package genetic.engines.sequential

import genetic.engines.Progress
import genetic.genotype.Fitness.Rated
import genetic.genotype.{Join, Variation}
import genetic.{Epoch, Population}

object SeqProgress extends Progress {
  def nextGeneration[A: Join : Variation](ratedPop: Population[Rated[A]],
                                          epoch: Epoch): Population[A] = epoch match {
    case Epoch(selection, crossover, mutation) =>
      mutation.generation(crossover.generation(selection.generation(ratedPop)))
  }
}
