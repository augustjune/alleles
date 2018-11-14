package genetic.engines.sequential

import genetic.engines.EvolutionStrategy
import genetic.genotype.{Join, Modification}
import genetic.{OperatorSet, Population}

trait SeqEvolutionStrategy extends EvolutionStrategy {
  def evolutionStep[G: Join : Modification](scoredPop: Population[(G, Double)],
                                            operators: OperatorSet): Population[G] =
    operators.mutation.generation(
      operators.crossover.generation(
        operators.selection.generation(scoredPop)
      )
    )
}
