package genetic.engines.sequential

import genetic.{OperatorSet, Population}
import genetic.engines.EvolutionEngine
import genetic.genotype.{Join, Modification}

trait SeqEvolutionStrategy extends EvolutionEngine {
  def evolutionStep[G: Join : Modification](scoredPop: Population[(G, Double)],
                                            operators: OperatorSet): Population[G] =
    operators.mutation.generation(
      operators.crossover.generation(
        operators.selection.generation(scoredPop)
      )
    )
}
