package genetic.engines

import akka.stream.scaladsl.Source
import genetic.Population
import genetic.engines.async.AsyncFitnessDriver
import genetic.engines.bestTracking.BestTrackingDriver
import genetic.genotype.{Fitness, Join, Variation}

import scala.concurrent.ExecutionContext

class CompositeDriver(fitnessEvaluator: FitnessEvaluator, flow: Evolution) extends EvolutionEnvironment {
  def evolve[A: Fitness : Join : Variation](options: EvolutionOptions[A]): EvolutionFlow[Population[A]] =
    Source.repeat(()).scan(options.initialPopulation) {
      case (prev, _) => flow.nextGeneration(fitnessEvaluator.rate(prev), options.operators)
    }

  def bestTracking: BestTrackingDriver = new BestTrackingDriver(fitnessEvaluator, flow)

  def async(implicit executionContext: ExecutionContext): AsyncFitnessDriver = new AsyncFitnessDriver(flow)
}
