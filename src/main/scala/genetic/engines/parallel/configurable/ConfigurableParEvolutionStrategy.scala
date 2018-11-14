package genetic.engines.parallel.configurable

import genetic.engines.EvolutionStrategy
import genetic.genotype.{Join, Modification}
import genetic.{OperatorSet, Population}

import scala.collection.parallel.TaskSupport
import scala.collection.parallel.immutable.ParVector

trait ConfigurableParEvolutionStrategy extends EvolutionStrategy {
  protected val configuration: TaskSupport

  def evolutionStep[G: Join : Modification](scoredPop: Population[(G, Double)],
                                            operators: genetic.OperatorSet): Population[G] = operators match {
    case OperatorSet(selection, crossover, mutation) =>
      val base = ParVector.fill(scoredPop.size / 2)(())
      base.tasksupport = configuration
      base.map(_ => selection.single(scoredPop))
        .flatMap(crossover.single(_))
        .map(mutation.single(_))
        .seq
  }
}

