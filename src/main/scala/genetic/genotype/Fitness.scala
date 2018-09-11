package genetic.genotype

/**
  * Fitness function for instances of type `A`, which computes
  * how close a given design solution is to achieving the set aims
  * Note: The smaller - the better
  */
trait Fitness[-A] {
  def value(a: A): Int
}

object Fitness {
  /**
    * Applies trait function from the implicit scope;
    * allows to use trait as context bounds
    */
  def apply[A](a: A)(implicit f: Fitness[A]): Int = f.value(a)

  implicit class FitnessObj[A](a: A)(implicit f: Fitness[A]) {
    def fitness: Int = f.value(a)
  }
}
