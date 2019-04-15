package alleles.environment.bestTracking

import akka.stream.scaladsl.Source
import alleles.environment.{EvolutionFlow, Progress, Ranking}
import alleles.genotype.{Fitness, Join, Variation}
import alleles.{Epoch, Population}

/**
  * Implementation of genetic algorithm with parametrized way of rating the population
  * and applying genetic operators to it, capable of keeping track of the best individual
  * through whole evolution with no computation overhead
  */
class BestTrackingSetting(ranking: Ranking, flow: Progress) {
  def evolve[A: Fitness : Join : Variation](initial: Population[A],
                                            operators: Epoch[A]): EvolutionFlow[PopulationWithBest[A]] =
    Source.repeat(()).scan((initial, (initial.head, Double.MaxValue))) {
      case ((prev, prevBest), _) =>
        val ratedPopulation = ranking.rate(prev)
        (flow.nextGeneration(ratedPopulation, operators), (prevBest +: ratedPopulation).minBy(_._2))
    }
}
