package genetic.engines.parallel

import cats.Functor
import genetic.Population
import genetic.engines.EvolutionEngine

/**
  * A version of EvolutionEngine with default parallel fitness computation technique
  */
trait ParallelEvolutionEngine extends EvolutionEngine {
  override val populationFunctor: Functor[Population] = new Functor[Population] {
    def map[A, B](fa: Population[A])(f: A => B): Population[B] = fa.par.map(f).seq
  }
}
