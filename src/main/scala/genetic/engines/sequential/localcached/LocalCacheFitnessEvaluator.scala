package genetic.engines.sequential.localcached

import cats.Functor
import genetic.Population
import genetic.engines.FitnessEvaluator

object LocalCacheFitnessEvaluator extends  {
  private val populationFunctor: Functor[Population] = new Functor[Population] {
    def map[A, B](fa: Population[A])(f: A => B): Population[B] = {
      val cache = fa.map(x => (x, x)).toMap.mapValues(f)
      fa.map(cache)
    }
  }
} with FitnessEvaluator(populationFunctor)
