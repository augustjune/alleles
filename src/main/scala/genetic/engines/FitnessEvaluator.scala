package genetic.engines

import cats.Functor
import genetic.Population
import genetic.genotype.Fitness

trait FitnessEvaluator {
  /**
    * Functor upon which Fitness value is going to be evaluated for each population,
    * with standard scala implementation by default
    */
  protected val populationFunctor: Functor[Population]

  def rate[G: Fitness](population: Population[G]): Population[Rated[G]] =
    populationFunctor.map(population)(g => g -> Fitness(g))
}
