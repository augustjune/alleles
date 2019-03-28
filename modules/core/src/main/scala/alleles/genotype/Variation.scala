package alleles.genotype

/**
  * Any function which allows to make a new instance of type `A`
  * which differs from the original one in random place
  *
  * Laws:
  *   1. Modified instance does not equal to original one
  *     modify(g) != g
  *   2. After a certain number of modification the same input produces different outputs
  *     def modify5(g: A) = modify(modify(modify(modify(modify(g)))))
  *     modify5(g) != modify5(g)
  */
trait Variation[A] {
  def modify(ind: A): A
}

object Variation {
  /**
    * Applies trait function from the implicit scope;
    * allows to use trait as context bounds
    */
  def apply[A](ind: A)(implicit m: Variation[A]): A = m.modify(ind)
}
