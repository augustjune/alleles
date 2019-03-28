package alleles.environment.sequential

import cats.Functor
import alleles.Population
import alleles.environment.Ranking

object SeqRanking extends {
  private val sequentialFunctor = new Functor[Population] {
    def map[A, B](fa: Population[A])(f: A => B): Population[B] = fa.map(f)
  }
} with Ranking(sequentialFunctor)
