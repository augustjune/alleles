package genetic.genotype

/**
  * Fitness function for instances of type `G`, which computes
  * how close a given design solution is to achieving the set aims
  * Note: The smaller - the better
  */
trait Fitness[-G] {
  def value(g: G): Int
}

object Fitness {
  /**
    * Applies trait function from the implicit scope;
    * allows to use trait as context bounds
    */
  def apply[G](g: G)(implicit f: Fitness[G]): Int = f.value(g)
}
