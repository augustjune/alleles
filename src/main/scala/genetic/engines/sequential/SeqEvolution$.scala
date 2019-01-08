package genetic.engines.sequential

import genetic.engines.{Evolution, Rated}
import genetic.genotype.{Join, Modification}
import genetic.{OperatorSet, Population}

object SeqEvolution$ extends Evolution {
  def nextGeneration[G: Join : Modification](ratedPop: Population[Rated[G]],
                                             operators: OperatorSet): Population[G] =
    operators.mutation.generation(
      operators.crossover.generation(
        operators.selection.generation(ratedPop)
      )
    )
}
