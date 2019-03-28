package alleles.environment.sequential.localcached

import cats.Functor
import alleles.Population
import alleles.environment.Ranking

object LocalCacheRanking extends  {
  private val cachedRanking: Functor[Population] = new Functor[Population] {
    def map[A, B](fa: Population[A])(f: A => B): Population[B] = {
      val cache = fa.map(x => (x, x)).toMap.mapValues(f)
      fa.map(cache)
    }
  }
} with Ranking(cachedRanking)
