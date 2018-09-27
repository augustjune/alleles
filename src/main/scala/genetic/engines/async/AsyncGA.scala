package genetic.engines.async

import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import cats.Semigroup
import genetic.genotype.{Fitness, Modification}
import genetic.{GeneticAlgorithmTemplate, OperatorSet, Population}

import scala.concurrent.{ExecutionContext, Future}

class AsyncGA(parallelism: Int)(implicit mat: ActorMaterializer, exContext: ExecutionContext) extends GeneticAlgorithmTemplate[Future] {

  /**
    * Evolves initial population with genetic operators for a finite number of iterations
    *
    * @param operators  Set of genetic operators which define iterative cycle
    * @param iterations Number of iterations
    * @return Evolved population
    */

  def evolve[G: Fitness : Semigroup : Modification](population: Population[G],
                                                    operators: OperatorSet,
                                                    iterations: Int): Future[Population[G]] = {
    val iterationStage = stage[G](population.size, operators) _

    def loop(pop: Future[Population[G]], n: Int): Future[Population[G]] =
      if (n < iterations) loop(pop.flatMap(iterationStage(_)), n + 1)
      else pop

    loop(Future.successful(population), 0)
  }

  protected def stage[G: Fitness : Semigroup : Modification](size: Int, operators: OperatorSet)
                                                            (population: Population[G]): Future[Population[G]] = operators match {
    case OperatorSet(selection, crossover, mutation) =>
      Source(1 to size)
        .mapAsyncUnordered(parallelism)(_ => Future(selection.single(population)))
        .mapAsyncUnordered(parallelism)(parents => Future(crossover.single(parents)))
        .mapConcat(identity)
        .mapAsyncUnordered(parallelism)(x => Future(mutation.single(x)))
        .runWith(Sink.seq[G])
  }
}
