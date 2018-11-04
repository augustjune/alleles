package genetic.engines.parallel

import genetic.engines.EvolutionEngine
import genetic.genotype.{Fitness, Join, Modification}
import genetic.{OperatorSet, Population}

import scala.collection.parallel.TaskSupport
import scala.collection.parallel.immutable.ParVector

class ConfigurableParallelEvolutionEngine(taskSupport: TaskSupport) extends EvolutionEngine {
  def evalFitnesses[G: Fitness](population: Population[G]): Population[(G, Double)] = {
    val parPop = population.par
    parPop.tasksupport = taskSupport
    parPop.map(g => g -> Fitness(g)).seq
  }

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

