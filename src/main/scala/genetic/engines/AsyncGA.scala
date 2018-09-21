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

class AsyncGA(parallelism: Int)(implicit mat: ActorMaterializer, exContext: ExecutionContext) extends GeneticAlgorithm[Future] {

  protected def evolve[G: Fitness : Semigroup : Modification, B]
  (settings: AlgoSettings[G])
  (start: B, until: (B, Population[G]) => Boolean, click: B => B): Future[Population[G]] = {
    val selection = settings.selection
    val crossover = settings.crossover
    val mutation = settings.mutation

    def loop(futPop: Future[Population[G]], condition: B): Future[Population[G]] = {
      futPop.flatMap { pop =>
        if (until(condition, pop)) {
          loop(stage(pop, pop.size)(selection, crossover, mutation)(parallelism), click(condition))
        } else futPop
      }
    }

    loop(Future.successful(settings.initial), start)
  }

  private def stage[G: Fitness : Semigroup : Modification](population: Population[G], size: Int)
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
