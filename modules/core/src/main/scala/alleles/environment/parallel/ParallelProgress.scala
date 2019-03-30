package alleles.environment.parallel

import alleles.environment.Progress
import alleles.genotype.Fitness.Rated
import alleles.genotype.{Join, Variation}
import alleles.{Epoch, Population}

import scala.collection.parallel.immutable.ParVector

object ParallelProgress extends Progress {
  def nextGeneration[A: Join : Variation](ratedPop: Population[Rated[A]],
                                          epoch: Epoch[A]): Population[A] = epoch match {
    case Epoch(selection, crossover, mutation) =>
      ParVector.fill(ratedPop.size / 2)(())
        .map(_ => selection.pair(ratedPop))
        .flatMap((crossover.pair _).tupled)
        .map(mutation.single)
        .seq
  }

}
