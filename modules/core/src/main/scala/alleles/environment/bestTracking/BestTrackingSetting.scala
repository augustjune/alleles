package alleles.environment.bestTracking

import alleles.environment.{EvolutionFlow, Progress, Ranking}
import alleles.genotype.{Fitness, Join, Variation}
import alleles.{Epoch, Population}
import fs2.Stream

/**
  * Implementation of genetic algorithm with parametrized way of rating the population
  * and applying genetic operators to it, capable of keeping track of the best individual
  * through whole evolution with no computation overhead
  */
class BestTrackingSetting[A: Fitness : Join : Variation](ranking: Ranking[A], flow: Progress[A]) {
  def evolve(initial: Population[A], operators: Epoch[A]): EvolutionFlow[PopulationWithBest[A]] =
    Stream(()).repeat.scan((initial, (initial.head, Double.MaxValue))) {
      case ((prev, prevBest), _) =>
        val ratedPopulation = ranking.rate(prev)
        (flow.nextGeneration(ratedPopulation, operators), (prevBest +: ratedPopulation).minBy(_._2))
    }
}
