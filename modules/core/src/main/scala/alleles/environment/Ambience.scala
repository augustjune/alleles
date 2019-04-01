package alleles.environment

import alleles.Population
import alleles.genotype.{Fitness, Join, Variation}

// ToDo - add documentation
trait Ambience {
  def evolve[A: Fitness : Join : Variation](epic: Epic[A]): EvolutionFlow[Population[A]]
}
