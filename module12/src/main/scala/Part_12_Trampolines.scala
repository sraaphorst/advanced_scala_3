/** Scala has its own version of trampolines in scala.util.control.TailCalls.
 * Building blocks of many functional patterns.
 * Can be very slow. See fib here for an example.
 */

import scala.util.control.TailCalls._

object Part_12_Trampolines extends App {
  // Determine if a list contains an even or odd number of elements.
  def isEven(xs: List[Int]): TailRec[Boolean] =
    if (xs.isEmpty) done(true) else tailcall(isOdd(xs.tail))

  def isOdd(xs: List[Int]): TailRec[Boolean] =
    if (xs.isEmpty) done(false) else tailcall(isEven(xs.tail))

  // Fibonacci using trampolines.
  def fib(n: Int): TailRec[BigInt] =
    if (n < 2) done(n) else for {
      x <- tailcall(fib(n-1))
      y <- tailcall(fib(n-2))
    } yield x + y

  // Use LazyList in Scala 2.13.
  for (i <- Stream.from(1))
    println(s"$i -> ${fib(i).result}") // use x.result instead of trampoline(x).
}
