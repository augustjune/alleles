package genetic.engines.parallel.configurable

import cats.Functor
import genetic.Population
import genetic.engines.EvolutionEngine

import scala.collection.parallel.TaskSupport

trait ConfigurableParFitnessEvaluator extends EvolutionEngine {
  val taskSupport: TaskSupport

  /**
    * Functor upon which Fitness value is going to be evaluated for each population,
    * with standard scala implementation by default
    */
  val populationFunctor: Functor[Population] = new Functor[Population] {
    def map[A, B](fa: Population[A])(f: A => B): Population[B] = {
      val parPop = fa.par
      parPop.tasksupport = taskSupport
      parPop.map(f).seq
    }
  }
}
