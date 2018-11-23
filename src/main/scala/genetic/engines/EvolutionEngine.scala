package genetic.engines

import akka.NotUsed
import akka.stream.scaladsl.Source
import cats.Functor
import genetic.engines.async.AsyncFitnessEvolution
import genetic.engines.bestTracking.BestTrackingEvolution
import genetic.genotype.{Fitness, Join, Modification}
import genetic.{OperatorSet, Population}

import scala.concurrent.ExecutionContext

class EvolutionEngine(fitnessEvaluator: FitnessEvaluator, strategy: EvolutionStrategy) {
  def evolve[G: Fitness : Join : Modification](options: EvolutionOptions[G]): Source[Population[G], NotUsed] =
    Source.repeat(()).scan(options.initialPopulation) {
      case (prev, _) => strategy.evolutionStep(fitnessEvaluator.rate(prev), options.operators)
    }

  def bestTracking: BestTrackingEvolution = new BestTrackingEvolution(fitnessEvaluator, strategy)

  def async(implicit executionContext: ExecutionContext): AsyncFitnessEvolution = new AsyncFitnessEvolution(strategy)
}
