package genetic.engines.sequential

import genetic.engines.Evolution
import genetic.genotype.Fitness.Rated
import genetic.genotype.{Join, Variation}
import genetic.{OperatorSet, Population}

object SeqEvolution$ extends Evolution {
  def nextGeneration[A: Join : Variation](ratedPop: Population[Rated[A]],
                                          operators: OperatorSet): Population[A] =
    operators.mutation.generation(
      operators.crossover.generation(
        operators.selection.generation(ratedPop)
      )
    )
}
