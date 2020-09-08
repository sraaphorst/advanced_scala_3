object Part_04_ImplementingOption extends App {
  // Covariant in T, meaning that if U is a subtype of T, then Optional[U] is a subtype of Optional[T].
  // To satisfy LUP (least upper bounds), extends Product with Serializable
  sealed abstract class Optional[+T] extends Product with Serializable {
    def isDefined: Boolean
    def map[U](f: T => U): Optional[U]                // Functor
    def flatMap[U](f: T => Optional[U]): Optional[U]  // Monad (also Apply)
    def withFilter(p: T => Boolean): Optional[T]
  }

  /**
   * Functors
   * .map[T, U](f: T => U)(M[T]) => M[U], usually implemented on the class itself in Scala as with Optional.
   * In other languages, implemented as typeclasses.
   * More correct to say that a type HAS a functor rather than a type IS a functor.
   * Cats has a functor typeclass with a signature like the above.
   *
   * Functor Laws:
   * 1. x map id = x (identity)
   * 2. x map (a andThen b) = (x map a) map b // composition
   */

  /**
   * Monads
   * .flatMap[T, U](f: T => M[U])(M[T]) => M[U]
   *
   * Monad Laws:
   * 1. apply(x) flatMap f = f(x) // left id: putting x in a monad and flatmapping f results in f(x)
   * 2. apply(x) flatMap apply = apply(x) // right id: putting x in a monad and flatmapping apply results in apply
   * 3. (apply(x) flatMap f) flatMap g = apply(x) flatMap(x => f(x) flatMap g) // associativity
   *    (a op b) op c == a op (b op c)
   *    putting x in a monad and then flatmapping f then g is the same as
   *    putting x in a monad and then flatmapping x => f(x) flatMap g
   */
  assert(Item(5).flatMap(x => Item(x * 2)) == Item(5 * 2))
  assert(Item(5).flatMap(Item.apply) == Item(5))
  assert((Item(5).flatMap(x => Item(x * 2))).flatMap(y => Item(y + 3)) ==
         Item(5).flatMap(x => Item(x * 2).flatMap(y => Item(y + 3)))) // the right side is a function

  case class Item[+T](value: T) extends Optional[T] {
    override val isDefined = true
    override def map[U](f: T => U): Optional[U] = Item(f(value))
    override def flatMap[U](f: T => Optional[U]): Optional[U] = f(value)
    override def withFilter(p: T => Boolean): Optional[T] = if (p(value)) this else Nada
  }

  // Note the Nothing here! Due to covariance, since Nothing is the subtype of all Scala types,
  // Optional[Nothing] is a subtype of Optional[T] for any type T.
  case object Nada extends Optional[Nothing] {
    override val isDefined: Boolean = false
    override def map[U](f: Nothing => U): Optional[U] = Nada
    override def flatMap[U](f: Nothing => Optional[U]): Optional[U] = Nada
    override def withFilter(p: Nothing => Boolean): Optional[Nothing] = Nada
  }


  case class Address(street: String, city: String, state: String, zipCode: String)
  case class Person(first: String, last: String, address: Optional[Address])

  // All of these generators should be of the same type, and it will yield that generator.
  // In this case, Option.
  // Each of these generators becomes a flatMap except the last, which is a map.
  // By mixing types, the map / flatMap types don't work.
  // The GUARD gets turned into a call to withFilter.
  def zipForPerson(startsWith: String)(op: Optional[Person]): Optional[String] = for {
    p <- op
    if p.first.startsWith(startsWith) // a GUARD
    a <- p.address
  } yield a.zipCode

  val people: List[Optional[Person]] = List(
    Item(Person("Fred", "Bloggs", Nada)),
    Item(Person("Simon", "Jones",
      Item(Address("123 Main", "Fakesville", "AZ", "12345")))),
    Nada
  )

  println(people.map(zipForPerson("S")))
  for (p <- people)
    println(p.flatMap(_.address).map(_.zipCode))
}
