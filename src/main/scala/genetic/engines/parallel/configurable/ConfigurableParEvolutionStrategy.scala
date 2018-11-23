package genetic.engines.parallel.configurable

import genetic.engines.{EvolutionStrategy, Rated}
import genetic.genotype.{Join, Modification}
import genetic.{OperatorSet, Population}

import scala.collection.parallel.TaskSupport
import scala.collection.parallel.immutable.ParVector

class ConfigurableParEvolutionStrategy(configuration: TaskSupport) extends EvolutionStrategy {

  def evolutionStep[G: Join : Modification](ratedPop: Population[Rated[G]],
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

