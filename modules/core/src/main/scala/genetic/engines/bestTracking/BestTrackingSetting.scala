package genetic.engines.bestTracking

import akka.stream.scaladsl.Source
import genetic.engines.{Progress, EvolutionFlow, Ranking}
import genetic.genotype.{Fitness, Join, Variation}
import genetic.{Epoch, Population}

class BestTrackingSetting(fitnessEvaluator: Ranking, flow: Progress) {
  def evolve[A: Fitness : Join : Variation](initial: Population[A],
                                            operators: Epoch): EvolutionFlow[PopulationWithBest[A]] =
    Source.repeat(()).scan((initial, (initial.head, Double.MaxValue))) {
      case ((prev, prevBest), _) =>
        val ratedPopulation = fitnessEvaluator.rate(prev)
        (flow.nextGeneration(ratedPopulation, operators), (prevBest +: ratedPopulation).minBy(_._2))
    }
}
