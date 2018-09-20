case class ImplicitF[In, Out, Implicit](f: Implicit => In => Out) {
  def apply(in: In)(implicit i: Implicit): Out = f(i)(in)
}

/*def expand[G]: ImplicitF[Population[(G, G)], Population[G], Semigroup[G]] =
      ImplicitF(i => _.flatMap(apply(_)(i)))*/
