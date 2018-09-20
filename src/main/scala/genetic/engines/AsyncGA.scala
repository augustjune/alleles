package genetic.engines

import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import genetic.collections.IterablePair
import genetic.genotype.{Fitness, Mutation}
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
  def evolveUntilReached[G: Fitness](settings: AlgoSettings[G], fitnessThreshold: Int): ParallelizableFuture[Population[G]] = ???

  protected def evolve[G, B](settings: AlgoSettings[G])(start: B, until: B => Boolean, click: B => B): ParallelizableFuture[Population[G]] = ???

  def stage[G](population: Population[G], size: Int)
              (selection: Selection[G], crossover: Crossover[G], mutation: Mutation[G])
              (parallelism: Int): Future[Seq[G]] = {
    Source(1 to size)
      .mapAsyncUnordered(parallelism)(_ => Future(selection(population)))
      .mapAsyncUnordered(parallelism){ case (left, right) => Future(IterablePair(crossover(left, right), crossover(right, left)))}
      .mapConcat(identity)
      .mapAsyncUnordered(parallelism)(x => Future(mutation.modify(x)))
      .runWith(Sink.seq[G])
  }
}
