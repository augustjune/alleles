package genetic.engines.parallel

import cats.Functor
import genetic.Population
import genetic.engines.FitnessEvaluator

object ParallelFitnessEvaluator extends {
  private val populationFunctor: Functor[Population] = new Functor[Population] {
    def map[A, B](fa: Population[A])(f: A => B): Population[B] = fa.par.map(f).seq
  }
} with FitnessEvaluator(populationFunctor)
