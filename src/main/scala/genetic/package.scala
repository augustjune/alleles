import akka.stream.ActorMaterializer
import cats.Semigroup
import genetic.engines.streaming.StreamingGA
import genetic.engines.sync.{BasicGA, ParallelGA}
import genetic.genotype.{Fitness, Modification}
import genetic.operators._

import scala.concurrent.ExecutionContext



package object genetic {
  type Population[+A] = Seq[A]

  /*object Population {
    def of[G: Scheme](n: Int): Population[G] = Scheme.make(n)
  }*/

  implicit class PopulationExtension[A](population: Population[A]) {
    def best(implicit f: Fitness[A]): A = population.minBy(f.value)
  }

  case class OperatorSet(selection: Selection, crossover: Crossover, mutation: Mutation) {
    def generation[G: Fitness : Semigroup : Modification](population: Population[G]): Population[G] =
      mutation.generation(crossover.generation(selection.generation(population)))
  }

  object GeneticAlgorithm extends BasicGA {
    val par: ParallelGA = new ParallelGA()

    def stream(implicit mat: ActorMaterializer, ex: ExecutionContext): StreamingGA = new StreamingGA()
  }
}
