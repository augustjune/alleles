package genetic.engines

import cats.Functor
import genetic.Population
import genetic.genotype.Fitness
import genetic.genotype.Fitness.Rated

class FitnessEvaluator(populationFunctor: Functor[Population]) {
  def rate[A: Fitness](population: Population[A]): Population[Rated[A]] =
    populationFunctor.map(population)(a => a -> Fitness(a))
}
