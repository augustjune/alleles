package genetic.engines.sequential

import genetic.engines.{EvolutionFlow, Rated}
import genetic.genotype.{Join, Modification}
import genetic.{OperatorSet, Population}

object SeqEvolutionFlow extends EvolutionFlow {
  def nextGeneration[G: Join : Modification](ratedPop: Population[Rated[G]],
                                             operators: OperatorSet): Population[G] =
    operators.mutation.generation(
      operators.crossover.generation(
        operators.selection.generation(ratedPop)
      )
    )
}
