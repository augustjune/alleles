package genetic.engines.bestTracking

import akka.stream.scaladsl.Source
import genetic.engines.{Evolution, EvolutionFlow, FitnessEvaluator}
import genetic.genotype.{Fitness, Join, Variation}
import genetic.{OperatorSet, Population}

class BestTrackingDriver(fitnessEvaluator: FitnessEvaluator, flow: Evolution) {
  def evolve[A: Fitness : Join : Variation](initial: Population[A],
                                            operators: OperatorSet): EvolutionFlow[PopulationWithBest[A]] =
    Source.repeat(()).scan((initial, (initial.head, Double.MaxValue))) {
      case ((prev, prevBest), _) =>
        val ratedPopulation = fitnessEvaluator.rate(prev)
        (flow.nextGeneration(ratedPopulation, operators), (prevBest +: ratedPopulation).minBy(_._2))
    }
}
