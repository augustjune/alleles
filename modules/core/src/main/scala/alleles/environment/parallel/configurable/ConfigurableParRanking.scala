package alleles.environment.parallel.configurable

import alleles.Population
import alleles.environment.FitnessRanking
import alleles.genotype.Fitness
import cats.Functor

import scala.collection.parallel.TaskSupport

/**
  * Parallel implementation of assigning fitness value for population individuals,
  * with configurable way of performing parallel computation
  */
class ConfigurableParRanking[A: Fitness](configuration: TaskSupport) extends {
  private val parallelFunctor: Functor[Population] = new Functor[Population] {
    def map[I, B](fa: Population[I])(f: I => B): Population[B] = {
      val parPop = fa.par
      parPop.tasksupport = configuration
      parPop.map(f).seq
    }
  }
} with FitnessRanking(parallelFunctor)
