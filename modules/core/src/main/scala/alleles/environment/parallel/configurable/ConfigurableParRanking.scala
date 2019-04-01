package alleles.environment.parallel.configurable

import alleles.Population
import alleles.environment.Ranking
import cats.Functor

import scala.collection.parallel.TaskSupport
// ToDo - add documentation
class ConfigurableParRanking(configuration: TaskSupport) extends {
  private val parallelFunctor: Functor[Population] = new Functor[Population] {
    def map[A, B](fa: Population[A])(f: A => B): Population[B] = {
      val parPop = fa.par
      parPop.tasksupport = configuration
      parPop.map(f).seq
    }
  }
} with Ranking(parallelFunctor)
