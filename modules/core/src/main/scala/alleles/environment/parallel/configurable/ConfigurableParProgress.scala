package alleles.environment.parallel.configurable

import alleles.environment.Progress
import alleles.genotype.Fitness.Rated
import alleles.genotype.{Join, Variation}
import alleles.{Epoch, Population}

import scala.collection.parallel.TaskSupport
import scala.collection.parallel.immutable.ParVector

/**
  * Parallel implementation of evolution progress,
  * with configurable way of performing parallel computation
  */
class ConfigurableParProgress[A: Join : Variation](configuration: TaskSupport) extends Progress[A] {

  def nextGeneration(ratedPop: Population[Rated[A]],
                                          epoch: Epoch[A]): Population[A] = epoch match {
    case Epoch(selection, crossover, mutation) =>
      val base = ParVector.fill(ratedPop.size / 2)(())
      base.tasksupport = configuration
      base.map(_ => selection.pair(ratedPop))
        .flatMap((crossover.pair _).tupled)
        .map(mutation.single)
        .seq
  }
}

