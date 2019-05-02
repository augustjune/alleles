import alleles.genotype.Fitness
import alleles.stages._

package object alleles {
  /**
    * Bag of individual genotypes used in genetic algorithm
    */
  type Population[+A] = Vector[A]

  implicit class PopulationExtension[A](private val population: Population[A]) extends AnyVal {
    def best(implicit f: Fitness[A]): A = population.minBy(f.value)

    def ranked(implicit f: Fitness[A]): Population[(A, Double)] =
      population.map(x => x -> f.value(x))
  }

  /**
    * Set of genetic operators which defines generation cycle
    */
  case class Epoch[A](selection: Selection, crossover: CrossoverStrategy[A], mutation: MutationStrategy[A])
}
