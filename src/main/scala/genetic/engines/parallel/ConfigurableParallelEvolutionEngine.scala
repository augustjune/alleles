package genetic.engines.parallel

import genetic.genotype.{Join, Modification}
import genetic.{OperatorSet, Population}

import scala.collection.parallel.TaskSupport
import scala.collection.parallel.immutable.ParVector

class ConfigurableParallelEvolutionEngine(taskSupport: TaskSupport) extends ParallelEvolutionEngine {
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

