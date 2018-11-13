package genetic.engines.bestTracking

import akka.NotUsed
import akka.stream.scaladsl.Source
import genetic.engines.EvolutionEngine
import genetic.genotype.{Fitness, Join, Modification}
import genetic.{OperatorSet, Population}

class BestTrackingEvolution(inner: EvolutionEngine) {
  def evolve[G: Fitness : Join : Modification](initial: Population[G],
                                               operators: OperatorSet): Source[PopulationWithBest[G], NotUsed] =
    Source.repeat(()).scan((initial, (initial.head, Double.MaxValue))) {
      case ((prev, prevBest), _) =>
        val withFitnesses = inner.evalFitnesses(prev)
        (inner.evolutionStep(withFitnesses, operators), (prevBest +: withFitnesses).minBy(_._2))
    }
}
