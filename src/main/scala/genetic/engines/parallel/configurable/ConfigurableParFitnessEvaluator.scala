package genetic.engines.parallel.configurable

import cats.Functor
import genetic.Population
import genetic.engines.FitnessEvaluator

import scala.collection.parallel.TaskSupport

class ConfigurableParFitnessEvaluator(configuration: TaskSupport) extends FitnessEvaluator {
  /**
    * Functor upon which Fitness value is going to be evaluated for each population,
    * with standard scala implementation by default
    */
  protected val populationFunctor: Functor[Population] = new Functor[Population] {
    def map[A, B](fa: Population[A])(f: A => B): Population[B] = {
      val parPop = fa.par
      parPop.tasksupport = configuration
      parPop.map(f).seq
    }
  }
}
