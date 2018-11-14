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

  def evalFitnesses[G: Fitness](population: Population[G]): Population[(G, Double)] =
    populationFunctor.map(population)(g => g -> Fitness(g))
}
