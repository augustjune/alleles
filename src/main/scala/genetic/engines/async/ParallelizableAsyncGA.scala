package genetic.engines.async

import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import cats.Semigroup
import genetic.{AlgoSettings, GeneticAlgorithm, Population}
import genetic.genotype._
import genetic.operators._

import scala.concurrent.{ExecutionContext, Future}

object ParallelizableAsyncGA extends GeneticAlgorithm[ParallelizableFuture] {


  /**
    * Evolves initial population with genetic operators for a finite number of iterations
    *
    * @param settings   Set of genetic operators which define iterative cycle
    * @param iterations Number of iterations
    * @return Evolved population
    */
  override def evolve[G: Fitness : Semigroup : Modification](settings: AlgoSettings[G], iterations: Int)
  : ParallelizableFuture[Population[G]] = {
    var i = 0
    val initial = settings.initial
    val size = initial.size / 2
    var pop = ParallelizableFuture.pure(settings.initial)
    val selection = settings.selection
    val crossover = settings.crossover
    val mutation = settings.mutation
    while (i < iterations) {
      pop = pop.flatMap(x => stage(x, size)(selection, crossover, mutation))
      i += 1
    }

    pop

  }

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
