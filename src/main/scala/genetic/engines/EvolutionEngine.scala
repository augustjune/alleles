package genetic.engines

import akka.NotUsed
import akka.stream.scaladsl.Source
import cats.Functor
import genetic.engines.async.AsyncFitnessEvolution
import genetic.engines.bestTracking.BestTrackingEvolution
import genetic.genotype.{Fitness, Join, Modification}
import genetic.{OperatorSet, Population}

import scala.concurrent.ExecutionContext

trait EvolutionEngine {
  /**
    * Functor upon which Fitness value is going to be evaluated for each population,
    * with standard scala implementation by default
    */
  val populationFunctor: Functor[Population]

  def evolve[G: Fitness : Join : Modification](options: EvolutionOptions[G]): Source[Population[G], NotUsed] =
    Source.repeat(()).scan(options.initialPopulation) {
      case (prev, _) => evolutionStep(evalFitnesses(prev), options.operators)
    }

  def evalFitnesses[G: Fitness](population: Population[G]): Population[(G, Double)] =
    populationFunctor.map(population)(g => g -> Fitness(g))

  def evolutionStep[G: Join : Modification](scoredPop: Population[(G, Double)],
                                            operators: OperatorSet): Population[G]

  def bestTracking: BestTrackingEvolution = new BestTrackingEvolution(this)

  def async(implicit executionContext: ExecutionContext): AsyncFitnessEvolution = new AsyncFitnessEvolution(this)
}
