package alleles.environment.sequential

import alleles.Population
import alleles.environment.FitnessRanking
import alleles.genotype.Fitness
import cats.Functor

/**
  * Sequential implementation of assigning fitness value to population individuals
  */
class SeqRanking[A: Fitness] extends {
  private val sequentialFunctor = new Functor[Population] {
    def map[I, O](fa: Population[I])(f: I => O): Population[O] = fa.map(f)
  }
} with FitnessRanking(sequentialFunctor)
