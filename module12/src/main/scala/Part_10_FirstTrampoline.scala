import Part_09_ADTsRecap._

import scala.annotation.tailrec

object Part_10_FirstTrampoline extends App {
  @tailrec
  def trampoline[A](bounce: Bounce[A]): A = bounce match {
    // The result of Call is an unresolved Bounce, but it does not descend into the stack.
    // Instead, we handle the next bounce on the next iteration, until we are Done.
    case Call(nextFunc) => trampoline(nextFunc())
    case Done(result)   => result
  }
}
