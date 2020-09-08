object Part_07_MutualCallingFunctions extends App {
  // What about mutual calling functions?
  // Example: establish if a number is even or odd.
  // Scala's tail call will not pick up this recursion.
  // Note the lack of recursion symbols.
  def even(num: Int): Boolean = {
    def even1(n: Int = num): Boolean =
      n == 0 || odd1(n - 1)

    def odd1(n: Int): Boolean =
      if (n == 0) false else even1(n - 1)

    even1()
  }

  for (i <- 0 until 10)
    println(s"$i is even: ${even(i)}")
}
