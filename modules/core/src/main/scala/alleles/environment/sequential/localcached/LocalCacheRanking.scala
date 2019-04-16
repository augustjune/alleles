package alleles.environment.sequential.localcached

import alleles.Population
import alleles.environment.Ranking
import cats.Functor

/**
  * Sequential implementation of assigning fitness value to population individuals,
  * with memoization of result for the same individuals among single population
  */
object LocalCacheRanking extends  {
  private val cachedRanking: Functor[Population] = new Functor[Population] {
    def map[A, B](fa: Population[A])(f: A => B): Population[B] = {
      val cache = fa.map(x => (x, x)).toMap.mapValues(f)
      fa.map(cache)
    }
  }
} with Ranking(cachedRanking)
