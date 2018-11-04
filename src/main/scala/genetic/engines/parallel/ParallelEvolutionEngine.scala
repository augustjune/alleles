package genetic.engines.parallel

import genetic.engines.EvolutionEngine
import genetic.genotype.{Fitness, Join, Modification}
import genetic.{OperatorSet, Population}

import scala.collection.parallel.immutable.ParVector

class ParallelEvolutionEngine extends EvolutionEngine {
  def evalFitnesses[G: Fitness](population: Population[G]): Population[(G, Double)] =
    population.par.map(g => g -> Fitness(g)).seq

  def evolutionStep[G: Join : Modification](scoredPop: Population[(G, Double)],
                                            operators: OperatorSet): Population[G] = operators match {
    case OperatorSet(selection, crossover, mutation) =>
      ParVector.fill(scoredPop.size / 2)(())
        .map(_ => selection.single(scoredPop))
        .flatMap(crossover.single(_))
        .map(mutation.single(_))
        .seq
  }
}

