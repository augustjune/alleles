package genetic.engines

import genetic.Population
import genetic.genotype.{Fitness, Join, Variation}


trait EvolutionEnvironment {
  def evolve[A: Fitness : Join : Variation](options: EvolutionOptions[A]): EvolutionFlow[Population[A]]
}
