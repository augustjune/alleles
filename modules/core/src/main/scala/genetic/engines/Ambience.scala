package genetic.engines

import genetic.Population
import genetic.genotype.{Fitness, Join, Variation}


trait Ambience {
  def evolve[A: Fitness : Join : Variation](epic: Epic[A]): EvolutionFlow[Population[A]]
}
