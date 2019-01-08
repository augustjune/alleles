package genetic.engines

import akka.NotUsed
import akka.stream.scaladsl.Source
import genetic.Population
import genetic.genotype.{Fitness, Join, Modification}


trait EvolutionEnvironment {
  def evolve[G: Fitness : Join : Modification](options: EvolutionOptions[G]): EvolutionFlow[Population[G]]
}
