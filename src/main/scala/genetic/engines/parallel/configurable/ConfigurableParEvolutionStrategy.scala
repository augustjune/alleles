package genetic.engines.parallel.configurable

import genetic.{OperatorSet, Population}
import genetic.engines.EvolutionEngine
import genetic.genotype.{Join, Modification}

import scala.collection.parallel.TaskSupport
import scala.collection.parallel.immutable.ParVector

trait ConfigurableParEvolutionStrategy extends EvolutionEngine {
  val taskSupport: TaskSupport

  def evolutionStep[G: Join : Modification](scoredPop: Population[(G, Double)],
                                            operators: genetic.OperatorSet): Population[G] = operators match {
    case OperatorSet(selection, crossover, mutation) =>
      val base = ParVector.fill(scoredPop.size / 2)(())
      base.tasksupport = taskSupport
      base.map(_ => selection.single(scoredPop))
        .flatMap(crossover.single(_))
        .map(mutation.single(_))
        .seq
  }
}

