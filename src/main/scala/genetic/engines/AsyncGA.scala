package genetic.engines

import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import cats.Semigroup
import genetic.genotype.{Fitness, Modification}
import genetic.operators._
import genetic.{AlgoSettings, GeneticAlgorithm, Population}

import scala.concurrent.{ExecutionContext, Future}

object AsyncGA {
  type ParallelizableFuture[A] = (Int => Future[A])
}


import AsyncGA.ParallelizableFuture
class AsyncGA(implicit mat: ActorMaterializer, exContext: ExecutionContext) extends GeneticAlgorithm[ParallelizableFuture]{
  /**
    * Evolves initial population with genetic operators until the population
    * reaches certain fitness threshold with at least one representative
    *
    * @param settings         Set of genetic operators which define iterative cycle
    * @param fitnessThreshold Threshold to be crossed
    * @return Evolved population
    */
  def evolveUntilReached[G: Fitness: Semigroup: Modification](settings: AlgoSettings[G], fitnessThreshold: Int): ParallelizableFuture[Population[G]] = ???

  protected def evolve[G: Fitness: Semigroup: Modification, B](settings: AlgoSettings[G])(start: B, until: B => Boolean, click: B => B): ParallelizableFuture[Population[G]] = ???

  def stage[G: Fitness: Semigroup: Modification](population: Population[G], size: Int)
                                                (selection: Selection, crossover: Crossover, mutation: Mutation)
                                                (parallelism: Int): Future[Population[G]] = {
    Source(1 to size)
      .mapAsyncUnordered(parallelism)(_ => Future(selection.single(population)))
      .mapAsyncUnordered(parallelism)(parents => Future(crossover.single(parents)))
      .mapConcat(identity)
      .mapAsyncUnordered(parallelism)(x => Future(mutation.single(x)))
      .runWith(Sink.seq[G])
  }
}
