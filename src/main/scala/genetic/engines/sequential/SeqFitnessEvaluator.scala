package genetic.engines.sequential

import cats.Functor
import genetic.Population
import genetic.engines.FitnessEvaluator

trait SeqFitnessEvaluator extends FitnessEvaluator {
  /**
    * Functor upon which Fitness value is going to be evaluated for each population,
    * with standard scala implementation by default
    */
  protected val populationFunctor: Functor[Population] = new Functor[Population] {
    def map[A, B](fa: Population[A])(f: A => B): Population[B] = fa.map(f)
  }
}
