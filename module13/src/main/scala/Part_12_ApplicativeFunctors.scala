import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

object Part_12_ApplicativeFunctors extends App {
  // Remember we had zipForPerson:
  // for { p <- op; a <- p.address } yield a.zipCode
  // The resolution of p.address relies on the resolution of p from op.

  val o1: Option[Int] = Some(1)
  val o2: Option[Int] = Some(2)
  val o3: Option[Int] = None

  // Here we don't have the dependency: the resolutions are independent.
  def addOptionalInts(oa: Option[Int], ob: Option[Int]): Option[Int] = for {
    a <- oa
    b <- ob
  } yield a + b

  println(addOptionalInts(o1, o2))
  println(addOptionalInts(o1, o3))

  // Applicative functors combine the results from containers into a single container
  // of the same type.
  // Example: zip for Option:
  object Options {
    def zip[A, B](o1: Option[A], o2: Option[B]): Option[(A, B)] = (o1, o2) match {
      case (Some(a), Some(b)) => Some(a, b)
      case _                  => None
    }
  }

  println(Options.zip(o1, o2))
  println(Options.zip(o1, o3))

  // Example using Futures.
  def timeIt[A](name: String)(f: => A): A = {
    val start = System.currentTimeMillis()
    val v = f
    println(s"$name took ${System.currentTimeMillis() - start} ms.")
    v
  }

  // b could depend on a, so b blocks and doesn't even start until a completes.
  // This is why evaluating fv1 takes over 2 seconds.
  val fv1: Future[Int] = for {
    a <- Future { Thread.sleep(1000); 1 }
    b <- Future { Thread.sleep(1000); 2 }
  } yield a + b

  timeIt(name="Future with for") {
    Await.result(fv1, 1.minute)
  }

  // What we want to do is start the futures at the same time since they are independent.
  //
  // Applicative function: apply a function to multiple things, each of them in a container, and give back
  // a single container.
  //
  // Example for Future is this zip function.
  // That is why THIS future only takes about 1 second.
  val fv2 =
    Future { Thread.sleep(1000); 1}
    .zip(Future { Thread.sleep(1000); 2}).map { case (a, b) => a + b }

  timeIt(name="Future with zip") {
    Await.result(fv2, 1.minute)
  }

  // What about with a function? Example: zipWith.
  // o1 and o2 are independent again.
  // This is known as an applicative functor.
  // Can be used with a monoid to implement validation libraries (I want all of the failures:
  // produce a list of failures and return that instead of quitting on the first failure).
  object Options2 {
    def zipWith[A, B, R](o1: Option[A], o2: Option[B])(f: (A, B) => R): Option[R] =
      Options.zip(o1, o2) match {
        case Some((a, b)) => Some(f(a, b))
        case _ => None
      }
  }

  println(Options2.zipWith(o1, o2)(_ + _))
  println(Options2.zipWith(o1, o3)(_ + _))

  // Scala doesn't have syntactical support for applicatives, so we need to do something like this,
  // where the zip is the intermediary to do the work.
  val fa = Future { Thread.sleep(1000); 1}
  val fb = Future { Thread.sleep(1000); 2}
  val f3 = for {
    (a, b) <- fa.zip(fb)
  } yield a + b

  timeIt("Future with zip 2") {
    Await.result(f3, 1.minute)
  }
}
