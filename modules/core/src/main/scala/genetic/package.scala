import genetic.genotype.{Fitness, Join, Variation}
import genetic.operators._

package object genetic {
  /**
    * Bag of individual genotypes used in genetic algorithm
    */
  type Population[+A] = Vector[A]

  implicit class PopulationExtension[A](population: Population[A]) {
    def best(implicit f: Fitness[A]): A = population.minBy(f.value)

    def withFitnesses(implicit f: Fitness[A]): Population[(A, Double)] =
      population.map(x => x -> f.value(x))

  }

  /**
    * Set of genetic operators which defines generation cycle
    */
  case class Epoch(selection: Selection, crossover: Crossover, mutation: Mutation)
}
