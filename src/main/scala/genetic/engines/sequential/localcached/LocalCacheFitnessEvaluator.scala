package genetic.engines.sequential.localcached

import cats.Functor
import genetic.Population
import genetic.engines.FitnessEvaluator

trait LocalCacheFitnessEvaluator extends FitnessEvaluator {
  /**
    * Evaluating fitness value only for unique values of population,
    * copying computed result to identical individuals
    */
  protected val populationFunctor: Functor[Population] = new Functor[Population] {
    def map[A, B](fa: Population[A])(f: A => B): Population[B] = {
      val cache = fa.map(x => (x, x)).toMap.mapValues(f)
      fa.map(cache)
    }
  }
}
