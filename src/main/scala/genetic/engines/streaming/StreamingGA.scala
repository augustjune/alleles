package genetic.engines.streaming

import akka.NotUsed
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import genetic.engines.sync.SynchronousGA
import genetic.genotype.{Fitness, Join, Modification}
import genetic.{GeneticAlgorithmTemplate, OperatorSet, Population}

import scala.concurrent.ExecutionContext
import scala.language.reflectiveCalls

/**
  * Akka-streams Source wrapper around synchronous genetic algorithm
  * Allows to run GA continuously, storing each population as element of the stream with
  * such features as backpressure and easy use as separate service via akka-http
  */
class StreamingGA(inner: SynchronousGA)(implicit mat: ActorMaterializer, ex: ExecutionContext)
  extends GeneticAlgorithmTemplate[({type T[A] = Source[A, NotUsed]})#T] {

  /**
    * Evolves initial population with certain set of genetic operators for n iterations
    */
  def evolve[G: Fitness : Join : Modification](population: Population[G],
                                               operators: OperatorSet,
                                               iterations: Int): Source[Population[G], NotUsed] =
    evolve(population, operators).take(iterations)

  /**
    * Continious evolution limited only by capability of downstream
    */
  def evolve[G: Fitness : Join : Modification](population: Population[G],
                                               operators: OperatorSet): Source[Population[G], NotUsed] =
    Source.repeat(()).scan(population) { case (prev, _) => inner.evolutionStep(prev, operators) }
}
