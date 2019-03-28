package genetic.engines.sequential

import genetic.engines.Progress
import genetic.genotype.Fitness.Rated
import genetic.genotype.{Join, Variation}
import genetic.{Epoch, Population}

object SeqProgress extends Progress {
  def nextGeneration[A: Join : Variation](ratedPop: Population[Rated[A]],
                                          operators: Epoch): Population[A] =
    operators.mutation.generation(
      operators.crossover.generation(
        operators.selection.generation(ratedPop)
      )
    )
}
