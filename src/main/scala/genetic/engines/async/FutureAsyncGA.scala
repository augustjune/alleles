package genetic.engines.async

import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import cats.Semigroup
import genetic.genotype.{Fitness, Modification}
import genetic.operators._
import genetic.{AlgoSettings, GeneticAlgorithm, Population}

import scala.concurrent.{ExecutionContext, Future}

class FutureAsyncGA(parallelism: Int)(implicit mat: ActorMaterializer, exContext: ExecutionContext) extends GeneticAlgorithm[Future] {


  /**
    * Evolves initial population with genetic operators for a finite number of iterations
    *
    * @param settings   Set of genetic operators which define iterative cycle
    * @param iterations Number of iterations
    * @return Evolved population
    */
  override def evolve[G: Fitness : Semigroup : Modification](settings: AlgoSettings[G], iterations: Int): Future[Population[G]] = {
    var i = 0
    val initial = settings.initial
    val size = initial.size / 2
    var pop = Future.successful(settings.initial)
    val selection = settings.selection
    val crossover = settings.crossover
    val mutation = settings.mutation
    while (i < iterations) {
      pop = pop.flatMap(stage(_, size)(selection, crossover, mutation))
      i += 1
    }

    pop
  }

  protected def evolve[G: Fitness : Semigroup : Modification, B]
  (settings: AlgoSettings[G])
  (start: B, until: (B, Population[G]) => Boolean, click: B => B): Future[Population[G]] = {
    val selection = settings.selection
    val crossover = settings.crossover
    val mutation = settings.mutation

    def loop(futPop: Future[Population[G]], condition: B): Future[Population[G]] = {
      futPop.flatMap { pop =>
        if (until(condition, pop)) {
          loop(stage(pop, pop.size)(selection, crossover, mutation), click(condition))
        } else futPop
      }
    }

    loop(Future.successful(settings.initial), start)
  }

  def stage[G: Fitness : Semigroup : Modification]
  (population: Population[G], size: Int)
  (selection: Selection, crossover: Crossover, mutation: Mutation): Future[Population[G]] = {
    Source(1 to size)
      .mapAsyncUnordered(parallelism)(_ => Future(selection.single(population)))
      .mapAsyncUnordered(parallelism)(parents => Future(crossover.single(parents)))
      .mapConcat(identity)
      .mapAsyncUnordered(parallelism)(x => Future(mutation.single(x)))
      .runWith(Sink.seq[G])
  }
}
