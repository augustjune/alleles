package genetic.engines.parallel

import genetic.engines.Progress
import genetic.genotype.Fitness.Rated
import genetic.genotype.{Join, Variation}
import genetic.{Epoch, Population}

import scala.collection.parallel.immutable.ParVector

object ParallelProgress extends Progress {
  def nextGeneration[A: Join : Variation](ratedPop: Population[Rated[A]],
                                          epoch: Epoch): Population[A] = epoch match {
    case Epoch(selection, crossover, mutation) =>
      ParVector.fill(ratedPop.size / 2)(())
        .map(_ => selection.single(ratedPop))
        .flatMap(crossover.single(_))
        .map(mutation.single(_))
        .seq
  }

}
