package genetic.engines.async

import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import cats.Semigroup
import genetic.{AlgoSettings, GeneticAlgorithm, Population}
import genetic.genotype._
import genetic.operators._

import scala.concurrent.{ExecutionContext, Future}

object ParallelizableAsyncGA extends GeneticAlgorithm[ParallelizableFuture] {

  protected def evolve[G: Fitness : Semigroup : Modification, B]
  (settings: AlgoSettings[G])
  (start: B, until: (B, Population[G]) => Boolean, click: B => B): ParallelizableFuture[Population[G]] = {
    val selection = settings.selection
    val crossover = settings.crossover
    val mutation = settings.mutation

    def loop(futPop: ParallelizableFuture[Population[G]], condition: B): ParallelizableFuture[Population[G]] = {
      futPop.flatMap { pop =>
        if (until(condition, pop)) {
          loop(stage(pop, pop.size)(selection, crossover, mutation), click(condition))
        } else futPop
      }
    }

    loop(ParallelizableFuture.pure(settings.initial), start)
  }

  def stage[G: Fitness : Semigroup : Modification]
  (population: Population[G], size: Int)
  (selection: Selection, crossover: Crossover, mutation: Mutation): ParallelizableFuture[Seq[G]] = ParallelizableFuture {
    implicit mat: ActorMaterializer =>
      implicit ex: ExecutionContext =>
        par: Int =>
          Source(1 to size)
            .mapAsyncUnordered(par)(_ => Future(selection.single(population)))
            .mapAsyncUnordered(par)(parents => Future(crossover.single(parents)))
            .mapConcat(identity)
            .mapAsyncUnordered(par)(x => Future(mutation.single(x)))
            .runWith(Sink.seq[G])
  }

}
