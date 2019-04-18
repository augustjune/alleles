package alleles.environment.sequential

import alleles.environment.Progress
import alleles.genotype.Fitness.Rated
import alleles.genotype.{Join, Variation}
import alleles.{Epoch, Population}

/**
  * Sequential implementation of evolution progress
  */
class SeqProgress[A: Join : Variation] extends Progress[A] {
  def nextGeneration(ratedPop: Population[Rated[A]], epoch: Epoch[A]): Population[A] =
    epoch match {
      case Epoch(selection, crossover, mutation) =>
        mutation.generation(crossover.generation(selection.generation(ratedPop)))
    }
}
