object Part_04_RecursiveFactorial extends App {
  // Recursive method: stack frames used to track stack, but they can overflow.
  def fact_recursive(n: Int): BigInt =
    if (n < 2) 1 else n * fact_recursive(n - 1)

  // This will overflow quickly.
  println("\n*** RECURSIVE ***")
  // Use LazyList in Scala 2.13.
  for (i <- Stream.from(1).filter(_ % 1000 == 0))
    println(s"$i -> ${fact_recursive(i)}")
}
