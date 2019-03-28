package genetic.engines.parallel.configurable

import genetic.{Epoch, Population}
import genetic.engines.Progress
import genetic.genotype.Fitness.Rated
import genetic.genotype.{Join, Variation}

import scala.collection.parallel.TaskSupport
import scala.collection.parallel.immutable.ParVector

class ConfigurableParProgress(configuration: TaskSupport) extends Progress {

  def nextGeneration[A: Join : Variation](ratedPop: Population[Rated[A]],
                                          operators: Epoch): Population[A] = {
    val base = ParVector.fill(ratedPop.size / 2)(())
    base.tasksupport = configuration
    base.map(_ => operators.selection.single(ratedPop))
      .flatMap(operators.crossover.single(_))
      .map(operators.mutation.single(_))
      .seq
  }
}

