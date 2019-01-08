package genetic.engines.bestTracking

import akka.NotUsed
import akka.stream.scaladsl.Source
import genetic.engines.{Evolution, FitnessEvaluator}
import genetic.genotype.{Fitness, Join, Modification}
import genetic.{OperatorSet, Population}

class BestTrackingDriver(fitnessEvaluator: FitnessEvaluator, flow: Evolution) {
  def evolve[G: Fitness : Join : Modification](initial: Population[G],
                                               operators: OperatorSet): Source[PopulationWithBest[G], NotUsed] =
    Source.repeat(()).scan((initial, (initial.head, Double.MaxValue))) {
      case ((prev, prevBest), _) =>
        val ratedPopulation = fitnessEvaluator.rate(prev)
        (flow.nextGeneration(ratedPopulation, operators), (prevBest +: ratedPopulation).minBy(_._2))
    }
}
