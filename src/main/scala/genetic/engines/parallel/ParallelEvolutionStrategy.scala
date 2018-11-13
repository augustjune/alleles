package genetic.engines.parallel

import genetic.{OperatorSet, Population}
import genetic.engines.EvolutionEngine
import genetic.genotype.{Join, Modification}

import scala.collection.parallel.immutable.ParVector

trait ParallelEvolutionStrategy extends EvolutionEngine {
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
