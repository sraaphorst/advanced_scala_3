object Part_03_Options extends App {

  case class Address(street: String, city: String, state: String, zipCode: String)
  case class Person(first: String, last: String, address: Option[Address])

  def zipForPerson(op: Option[Person]): Option[String] = for {
    p <- op
    a <- p.address
  } yield a.zipCode

  val people: List[Option[Person]] = List(
    Some(Person("Fred", "Bloggs", None)),
    Some(Person("Simon", "Jones",
      Some(Address("123 Main", "Fakesville", "AZ", "12345")))),
    None
  )

  println(people.map(zipForPerson))
  for (p <- people)
    println(p.flatMap(_.address).map(_.zipCode))
}
