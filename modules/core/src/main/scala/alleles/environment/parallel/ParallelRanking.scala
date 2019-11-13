package alleles.environment.parallel

import alleles.Population
import alleles.environment.FitnessRanking
import alleles.genotype.Fitness
import cats.Functor
import scala.collection.parallel.CollectionConverters._

/**
  * Parallel implementation of assigning fitness value for population individuals,
  * using parallel collections
  */
class ParallelRanking[A: Fitness] extends {
  private val parallelFunctor: Functor[Population] = new Functor[Population] {
    def map[I, B](fa: Population[I])(f: I => B): Population[B] = fa.par.map(f).seq
  }
} with FitnessRanking(parallelFunctor)
