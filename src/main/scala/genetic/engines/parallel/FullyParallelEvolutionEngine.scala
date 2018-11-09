package genetic.engines.parallel

import genetic.genotype.{Join, Modification}
import genetic.{OperatorSet, Population}

import scala.collection.parallel.immutable.ParVector

class FullyParallelEvolutionEngine extends ParallelEvolutionEngine {
  def evolutionStep[G: Join : Modification](scoredPop: Population[(G, Double)],
                                            operators: OperatorSet): Population[G] = operators match {
    case OperatorSet(selection, crossover, mutation) =>
      ParVector.fill(scoredPop.size / 2)(())
        .map(_ => selection.single(scoredPop))
        .flatMap(crossover.single(_))
        .map(mutation.single(_))
        .seq
  }

  /**
    *
    */
}

