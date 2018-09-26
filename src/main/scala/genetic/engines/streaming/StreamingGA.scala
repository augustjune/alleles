package genetic.engines.streaming

import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import cats.Semigroup
import genetic.genotype.{Fitness, Modification, Scheme}
import genetic.operators.{Crossover, Mutation, Selection}

import scala.concurrent.ExecutionContext

class StreamingGA(implicit mat: ActorMaterializer, ex: ExecutionContext) { //extends GeneticAlgorithm[({type T[A] = Source[A, NotUsed]})#T] {

  def evolution[G: Fitness : Semigroup : Modification]
  (popSize: Int, selection: Selection, crossover: Crossover, mutation: Mutation)
  (implicit scheme: Scheme[G]): Source[Seq[G], _] = {
    Source.fromIterator(() => Iterator.continually(scheme.create))
      .take(popSize)
      .fold(List[G]())((acc, x) => x :: acc)
      .flatMapConcat { pop: Seq[G] =>
        Source.repeat(()).scan(pop) { case (prev, _) =>
          mutation.generation(crossover.generation(selection.generation(prev)))
        }
      }

  }
}
