package genetic.engines

import cats.Functor
import genetic.Population
import genetic.genotype.Fitness

class FitnessEvaluator(populationFunctor: Functor[Population]) {
  def rate[G: Fitness](population: Population[G]): Population[Rated[G]] =
    populationFunctor.map(population)(g => g -> Fitness(g))
}
