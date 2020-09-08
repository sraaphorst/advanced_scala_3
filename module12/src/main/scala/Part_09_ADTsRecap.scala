object Part_09_ADTsRecap extends App {
  // We make a mini-language with Done indicating we are finished, and
  // Call indicating there is more to do.
  // The top trait is called Bounce because we will bounce off of the top of the stack
  // rather than descend into it (hence the term TRAMPOLINE).

  // ADT: Algebraic Data Type:
  // Some type T at the top of a hierarchy, with a controlled finite number of subtypes.
  // There can be infinite states in there (e.g. all the integers), but the subtypes are the
  // only things that can ever extend it.
  sealed trait Bounce[A]

  // We have a result: the computation is done with value result.
  case class Done[A](result: A) extends Bounce[A]

  // We still have calls to do: we call, and we could have other calls to do, or we could get
  // our result.
  case class Call[A](nextFunc: () => Bounce[A]) extends Bounce[A]

  // This is like a mini-DSL.
}
