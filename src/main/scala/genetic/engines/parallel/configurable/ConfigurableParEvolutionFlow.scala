package genetic.engines.parallel.configurable

import genetic.engines.{EvolutionFlow, Rated}
import genetic.genotype.{Join, Modification}
import genetic.{OperatorSet, Population}

import scala.collection.parallel.TaskSupport
import scala.collection.parallel.immutable.ParVector

class ConfigurableParEvolutionFlow(configuration: TaskSupport) extends EvolutionFlow {

  def nextGeneration[G: Join : Modification](ratedPop: Population[Rated[G]],
                                             operators: genetic.OperatorSet): Population[G] = operators match {
    case OperatorSet(selection, crossover, mutation) =>
      val base = ParVector.fill(ratedPop.size / 2)(())
      base.tasksupport = configuration
      base.map(_ => selection.single(ratedPop))
        .flatMap(crossover.single(_))
        .map(mutation.single(_))
        .seq
  }
}

