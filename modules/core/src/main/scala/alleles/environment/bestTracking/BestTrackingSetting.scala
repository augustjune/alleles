package alleles.environment.bestTracking

import akka.stream.scaladsl.Source
import alleles.environment.{Progress, EvolutionFlow, Ranking}
import alleles.genotype.{Fitness, Join, Variation}
import alleles.{Epoch, Population}

class BestTrackingSetting(ranking: Ranking, flow: Progress) {
  def evolve[A: Fitness : Join : Variation](initial: Population[A],
                                            operators: Epoch[A]): EvolutionFlow[PopulationWithBest[A]] =
    Source.repeat(()).scan((initial, (initial.head, Double.MaxValue))) {
      case ((prev, prevBest), _) =>
        val ratedPopulation = ranking.rate(prev)
        (flow.nextGeneration(ratedPopulation, operators), (prevBest +: ratedPopulation).minBy(_._2))
    }
}
