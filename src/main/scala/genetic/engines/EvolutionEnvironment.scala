package genetic.engines

import akka.NotUsed
import akka.stream.scaladsl.Source
import genetic.Population
import genetic.genotype.{Fitness, Join, Variation}


trait EvolutionEnvironment {
  def evolve[A: Fitness : Join : Variation](options: EvolutionOptions[A]): EvolutionFlow[Population[A]]
}
