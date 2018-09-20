package genetic.genotype

/**
  * Any function which allows to make a new instance of type `G`
  * which differs from the original one in random place
  *
  * Laws:
  *   1. Modified instance does not equal to original one
  *     modify(g) != g
  *   2. After a certain number of modification the same input produces different outputs
  *     def modify5(g: G) = modify(modify(modify(modify(modify(g)))))
  *     modify5(g) != modify5(g)
  */
trait Modification[G] {
  def modify(g: G): G
}

object Modification {
  /**
    * Applies trait function from the implicit scope;
    * allows to use trait as context bounds
    */
  def apply[G](g: G)(implicit m: Modification[G]): G = m.modify(g)
}
