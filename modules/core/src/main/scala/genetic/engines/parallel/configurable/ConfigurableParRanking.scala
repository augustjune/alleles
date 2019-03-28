package genetic.engines.parallel.configurable

import cats.Functor
import genetic.Population
import genetic.engines.Ranking

import scala.collection.parallel.TaskSupport

class ConfigurableParRanking(configuration: TaskSupport) extends {
  private val populationFunctor: Functor[Population] = new Functor[Population] {
    def map[A, B](fa: Population[A])(f: A => B): Population[B] = {
      val parPop = fa.par
      parPop.tasksupport = configuration
      parPop.map(f).seq
    }
  }
} with Ranking(populationFunctor)
