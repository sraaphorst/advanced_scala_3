import scala.annotation.tailrec

object Part_05_TailRecursiveFactorial extends App {
  // Tail recursive. Compiles to a proper while loop.
  def fact_tail_recursive(n: Int): BigInt = {
    // Annotation is not required, but it makes sure that it won't compile unless tail recursive.
    @tailrec
    def aux(i: Int = n, curr: BigInt = 1): BigInt = i match {
      case 0 | 1 => curr
      case _ => aux(i - 1, curr * i)
    }

    aux()
  }

  // This will not stack overflow and will have to be stopped manually.
  println("\n*** TAIL RECURSIVE ***")
  // Use LazyList in Scala 2.13.
  for (i <- Stream.from(1).filter(_ % 1000 == 0))
    println(s"$i -> ${fact_tail_recursive(i)}")
}
