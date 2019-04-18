package alleles.environment

import alleles.Population
import alleles.genotype.Fitness
import alleles.genotype.Fitness.Rated
import cats.Functor

/**
  * Applies traversing strategy in `populationFunctor` to calculate
  * fitness value to each individual in population
  */
class FitnessRanking[A: Fitness](populationFunctor: Functor[Population]) extends Ranking[A] {
  def rate(population: Population[A]): Population[Rated[A]] =
    populationFunctor.map(population)(a => a -> Fitness(a))
}
