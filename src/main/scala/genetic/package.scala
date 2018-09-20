import genetic.genotype.Fitness

package object genetic {
  type Population[+A] = Seq[A]

  implicit class PopulationExtension[A](population: Population[A]) {
    def best(implicit f: Fitness[A]): A = population.minBy(f.value)
  }
}
