import scala.util.control.TailCalls._

object Part_13_ScalaTailCalls extends App {
  // By-name parameters are evaluated every time they are used.
  // They won’t be evaluated at all if they are unused.
  def passByName(byName: => Int): Int = byName

  def even(n: Int): TailRec[Boolean] = n match {
    case 0 => done(true)
    case n => tailcall(odd(n-1))
  }

  def odd(n: Int): TailRec[Boolean] = n match {
    case 0 => done(false)
    case _ => tailcall(even(n-1))
  }

  println(passByName(37 - 3))
  for (i <- 0 to 10)
    println(s"$i -> ${even(i).result}")
}
