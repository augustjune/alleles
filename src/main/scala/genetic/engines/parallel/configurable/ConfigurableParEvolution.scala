package genetic.engines.parallel.configurable

import genetic.{OperatorSet, Population}
import genetic.engines.Evolution
import genetic.genotype.Fitness.Rated
import genetic.genotype.{Join, Variation}

import scala.collection.parallel.TaskSupport
import scala.collection.parallel.immutable.ParVector

class ConfigurableParEvolution(configuration: TaskSupport) extends Evolution {

  def nextGeneration[A: Join : Variation](ratedPop: Population[Rated[A]],
                                          operators: OperatorSet): Population[A] = {
    val base = ParVector.fill(ratedPop.size / 2)(())
    base.tasksupport = configuration
    base.map(_ => operators.selection.single(ratedPop))
      .flatMap(operators.crossover.single(_))
      .map(operators.mutation.single(_))
      .seq
  }
}

