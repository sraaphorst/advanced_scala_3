object Part_03_LoopFactorial extends App {
  // imperative factorial
  // Making these recursive is a fiction: will ultimately be run imperatively, as CPU
  // runs as a Turing machine. Idea is to put this off as long as possible.
  // Isolate iterative approaches, side effeccts, etc.
  // Recursion offers us an apparently fully functional immutable alternative.
  def fac_imperative(n: Int): BigInt = {
    var i = 1
    var acc: BigInt = 1

    while (i < n) {
      acc += i * acc
      i += 1
    }

    acc
  }

  // This will not overflow, since it uses loops and no stack trace.
  println("*** IMPERATIVE ***")
  for (i <- 0 until 10)
    println(s"$i -> ${fac_imperative(i)}")
}
