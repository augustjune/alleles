package alleles.environment

import alleles.Population
import alleles.genotype.{Fitness, Join, Variation}

/**
  * Representation of the environment, allowing the population of arbitrary individuals
  * to evolve within a set of genetic operators in a stream of adjusting populations
  */
trait Ambience {
  def evolve[A: Fitness : Join : Variation](epic: Epic[A]): EvolutionFlow[Population[A]]
}
