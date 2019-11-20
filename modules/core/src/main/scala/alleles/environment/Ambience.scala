package alleles.environment

import alleles.Population
import fs2.{Stream, Pure}

/**
  * Representation of the environment, allowing the population of arbitrary individuals
  * to evolve within a set of genetic operators in a stream of adjusting populations
  */
trait Ambience[A] {
  def evolve(epic: Epic[A]): Stream[Pure, Population[A]]
}
