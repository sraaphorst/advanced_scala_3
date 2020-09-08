import scala.annotation.tailrec

object Part_06_TailRecursiveFibonacci extends App {
  def fib_imperative(n: Int): BigInt = {
    var i: Int = 0
    var curr: BigInt = 1
    var prev: BigInt = 0

    for (_ <- 0 until n) {
      val newprev = curr
      curr += prev
      prev = newprev
    }

    prev
  }

  def fib_recursive(n: Int): BigInt = n match {
    case 0 => 0
    case 1 => 1
    case _ => fib_recursive(n-1) + fib_recursive(n-2)
  }

  def fib_tail_recursive(n: Int): BigInt = {
    @tailrec
    def aux(i: Int = 0, prev2: BigInt = 0, prev1: BigInt = 1): BigInt =
      if (n == i) prev2 else aux(i + 1, prev1, prev2 + prev1)

    aux()
  }

  println("*** IMPERATIVE ***")
  for (i <- 0 until 10)
    println(s"$i -> ${fib_imperative(i)}")

  println("\n*** RECURSIVE ***")
  for (i <- 0 until 10)
    println(s"$i -> ${fib_recursive(i)}")

  println("\n*** TAIL RECURSIVE ***")
  for (i <- 0 until 10)
    println(s"$i -> ${fib_tail_recursive(i)}")
}
