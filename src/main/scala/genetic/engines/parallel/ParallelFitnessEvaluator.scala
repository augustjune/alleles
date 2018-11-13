package genetic.engines.parallel

import cats.Functor
import genetic.Population
import genetic.engines.EvolutionEngine

trait ParallelFitnessEvaluator extends EvolutionEngine {
  /**
    * Functor upon which Fitness value is going to be evaluated for each population,
    * with standard scala implementation by default
    */
  val populationFunctor: Functor[Population] = new Functor[Population] {
    def map[A, B](fa: Population[A])(f: A => B): Population[B] = fa.par.map(f).seq
  }
}
