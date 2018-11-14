package genetic.engines

import akka.NotUsed
import akka.stream.scaladsl.Source
import cats.Functor
import genetic.engines.async.AsyncFitnessEvolution
import genetic.engines.bestTracking.BestTrackingEvolution
import genetic.genotype.{Fitness, Join, Modification}
import genetic.{OperatorSet, Population}

import scala.concurrent.ExecutionContext

trait EvolutionEngine extends FitnessEvaluator with EvolutionStrategy {
  def evolve[G: Fitness : Join : Modification](options: EvolutionOptions[G]): Source[Population[G], NotUsed] =
    Source.repeat(()).scan(options.initialPopulation) {
      case (prev, _) => evolutionStep(evalFitnesses(prev), options.operators)
    }

  def bestTracking: BestTrackingEvolution = new BestTrackingEvolution(this)

  def async(implicit executionContext: ExecutionContext): AsyncFitnessEvolution = new AsyncFitnessEvolution(this)
}
