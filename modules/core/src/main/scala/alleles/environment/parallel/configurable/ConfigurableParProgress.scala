package alleles.environment.parallel.configurable

import alleles.{Epoch, Population}
import alleles.environment.Progress
import alleles.genotype.Fitness.Rated
import alleles.genotype.{Join, Variation}

import scala.collection.parallel.TaskSupport
import scala.collection.parallel.immutable.ParVector

class ConfigurableParProgress(configuration: TaskSupport) extends Progress {

  def nextGeneration[A: Join : Variation](ratedPop: Population[Rated[A]],
                                          epoch: Epoch): Population[A] = epoch match {
    case Epoch(selection, crossover, mutation) =>
      val base = ParVector.fill(ratedPop.size / 2)(())
      base.tasksupport = configuration
      base.map(_ => selection.single(ratedPop))
        .flatMap(crossover.single(_))
        .map(mutation.single(_))
        .seq
  }
}

