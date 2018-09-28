import cats.Semigroup
import genetic.engines.sync.{BasicGA, ParallelGA, SynchronousGA}
import genetic.genotype.{Fitness, Modification}
import genetic.operators._

package object genetic {
  type Population[+A] = Seq[A]

  implicit class PopulationExtension[A](population: Population[A]) {
    def best(implicit f: Fitness[A]): A = population.minBy(f.value)
  }

  case class OperatorSet(selection: Selection, crossover: Crossover, mutation: Mutation) {
    def generation[G: Fitness : Semigroup : Modification](population: Population[G]): Population[G] =
      mutation.generation(crossover.generation(selection.generation(population)))
  }

  object GeneticAlgorithm extends BasicGA {
    val par: SynchronousGA = ParallelGA
  }
}
