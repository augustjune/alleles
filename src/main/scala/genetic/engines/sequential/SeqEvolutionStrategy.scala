package genetic.engines.sequential

import genetic.engines.{EvolutionStrategy, Rated}
import genetic.genotype.{Join, Modification}
import genetic.{OperatorSet, Population}

trait SeqEvolutionStrategy extends EvolutionStrategy {
  def evolutionStep[G: Join : Modification](ratedPop: Population[Rated[G]],
                                            operators: OperatorSet): Population[G] =
    operators.mutation.generation(
      operators.crossover.generation(
        operators.selection.generation(ratedPop)
      )
    )
}
