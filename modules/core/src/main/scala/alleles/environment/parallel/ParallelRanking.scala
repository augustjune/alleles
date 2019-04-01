package alleles.environment.parallel

import alleles.Population
import alleles.environment.Ranking
import cats.Functor
// ToDo - add documentation
object ParallelRanking extends {
  private val parallelFunctor: Functor[Population] = new Functor[Population] {
    def map[A, B](fa: Population[A])(f: A => B): Population[B] = fa.par.map(f).seq
  }
} with Ranking(parallelFunctor)
