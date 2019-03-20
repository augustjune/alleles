package genetic.engines.parallel.configurable

import genetic.engines.Evolution
import genetic.genotype.Fitness.Rated
import genetic.genotype.{Join, Variation}
import genetic.{OperatorSet, Population}

import scala.collection.parallel.TaskSupport
import scala.collection.parallel.immutable.ParVector

class ConfigurableParEvolution(configuration: TaskSupport) extends Evolution {

  def nextGeneration[A: Join : Variation](ratedPop: Population[Rated[A]],
                                          operators: genetic.OperatorSet): Population[A] = operators match {
    case OperatorSet(selection, crossover, mutation) =>
      val base = ParVector.fill(ratedPop.size / 2)(())
      base.tasksupport = configuration
      base.map(_ => selection.single(ratedPop))
        .flatMap(crossover.single(_))
        .map(mutation.single(_))
        .seq
  }
}

