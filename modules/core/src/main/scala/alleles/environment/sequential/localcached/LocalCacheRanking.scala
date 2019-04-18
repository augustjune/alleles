package alleles.environment.sequential.localcached

import alleles.Population
import alleles.environment.FitnessRanking
import alleles.genotype.Fitness
import cats.Functor

/**
  * Sequential implementation of assigning fitness value to population individuals,
  * with memoization of result for the same individuals among single population
  */
class LocalCacheRanking[A: Fitness] extends  {
  private val cachedRanking: Functor[Population] = new Functor[Population] {
    def map[I, B](fa: Population[I])(f: I => B): Population[B] = {
      val cache = fa.distinct.map(x => (x, f(x))).toMap
      fa.map(cache)
    }
  }
} with FitnessRanking(cachedRanking)
