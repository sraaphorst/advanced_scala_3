import Part_09_ADTsRecap._
import Part_10_FirstTrampoline._

object Part_11_EvenOddTrampoline extends App {
  def even(n: Int): Bounce[Boolean] = n match {
    case 0 => Done(true)

    // Higher order functions can:
    // 1. Take other functions
    // 2. Return other functions (work to be done). Here's the result: the result requires more work to be done.
    case _ => Call(() => odd(n-1))
  }

  def odd(n: Int): Bounce[Boolean] = n match {
    case 0 => Done(false)
    case _ => Call(() => even(n-1))
  }

  for (i <- 0 to 10) {
    // We have to wrap in trampoline because even and odd return Bounce, which is work to be done.
    println(s"$i -> ${if (trampoline(even(i))) "even" else if (trampoline(odd(i))) "odd" else "trash"}")
  }

  // Show that this is tail recursive so it never stack overflows.
  // Use LazyList in Scala 2.13.
  for (i <- Stream.from(1).filter(_ % 1001 == 0))
    println(s"$i -> ${trampoline(even(i))}")
}
