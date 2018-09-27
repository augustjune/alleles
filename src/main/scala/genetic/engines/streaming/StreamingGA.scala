package genetic.engines.streaming

import akka.NotUsed
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import cats.Semigroup
import genetic._
import genetic.genotype.{Fitness, Modification}

import scala.collection.parallel.immutable.ParVector
import scala.concurrent.ExecutionContext
import scala.language.reflectiveCalls

class StreamingGA(implicit mat: ActorMaterializer, ex: ExecutionContext)
  extends GeneticAlgorithmTemplate[({type T[A] = Source[A, NotUsed]})#T] {

  def evolve[G: Fitness : Semigroup : Modification](population: Population[G],
                                                    operators: OperatorSet,
                                                    iterations: Int): Source[Population[G], NotUsed] =
    evolve(population, operators).take(iterations)

  def evolve[G: Fitness : Semigroup : Modification](population: Population[G],
                                                    operators: OperatorSet): Source[Population[G], NotUsed] =
    operators match {
      case OperatorSet(selection, crossover, mutation) =>
        val parallelBase = ParVector.fill(population.size)(())
        Source.repeat(()).scan(population) { case (prev, _) =>
          parallelBase
            .map(_ => selection.single(prev))
            .flatMap(crossover.single(_))
            .map(mutation.single(_))
            .seq
        }
    }
}
