package genetic.engines

import akka.NotUsed
import akka.stream.scaladsl.Source
import genetic.Population
import genetic.engines.async.AsyncFitnessDriver
import genetic.engines.bestTracking.BestTrackingDriver
import genetic.genotype.{Fitness, Join, Modification}

import scala.concurrent.ExecutionContext

class CompositeDriver(fitnessEvaluator: FitnessEvaluator, flow: EvolutionFlow) extends EvolutionEnvironment {
  def evolve[G: Fitness : Join : Modification](options: EvolutionOptions[G]): Source[Population[G], NotUsed] =
    Source.repeat(()).scan(options.initialPopulation) {
      case (prev, _) => flow.nextGeneration(fitnessEvaluator.rate(prev), options.operators)
    }

  def bestTracking: BestTrackingDriver = new BestTrackingDriver(fitnessEvaluator, flow)

  def async(implicit executionContext: ExecutionContext): AsyncFitnessDriver = new AsyncFitnessDriver(flow)
}
