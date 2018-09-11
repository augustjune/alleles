package genetic.genotype

/**
  * Fitness function for instances of type `A`, which computes
  * how close a given design solution is to achieving the set aims
  * Note: The smaller - the better
  */
trait Fitness[-A] {
  def value(a: A): Int
}
