package genetic.engines.sequential

import cats.Functor
import genetic.Population
import genetic.engines.FitnessEvaluator

object SeqFitnessEvaluator extends {
  private val populationFunctor = new Functor[Population] {
    def map[A, B](fa: Population[A])(f: A => B): Population[B] = fa.map(f)
  }
} with FitnessEvaluator(populationFunctor)
