package alleles.environment

import alleles.Population
import alleles.genotype.Fitness
import alleles.genotype.Fitness.Rated
import cats.Functor
// ToDo - add documentation
class Ranking(populationFunctor: Functor[Population]) {
  def rate[A: Fitness](population: Population[A]): Population[Rated[A]] =
    populationFunctor.map(population)(a => a -> Fitness(a))
}
