package alleles.genotype

/**
  * Fitness function for instances of type `A`, which computes
  * how close a given design solution is to achieving the set aims
  * Note: The smaller - the better
  */
trait Fitness[A] {
  def value(a: A): Double

  def cached: CachedFitness[A] = new CachedFitness[A](this)
}

object Fitness {
  type Rated[A] = (A, Double)

  /**
    * Applies trait function from the implicit scope;
    * allows to use trait as context bounds
    */
  def apply[A](a: A)(implicit f: Fitness[A]): Double = f.value(a)
}
