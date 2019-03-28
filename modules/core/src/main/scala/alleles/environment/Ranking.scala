package alleles.environment

import cats.Functor
import alleles.Population
import alleles.genotype.Fitness
import alleles.genotype.Fitness.Rated

class Ranking(populationFunctor: Functor[Population]) {
  def rate[A: Fitness](population: Population[A]): Population[Rated[A]] =
    populationFunctor.map(population)(a => a -> Fitness(a))
}
