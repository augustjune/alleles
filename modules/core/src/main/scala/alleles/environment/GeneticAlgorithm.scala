package alleles.environment

import alleles.environment.parallel.configurable.{ConfigurableParProgress, ConfigurableParRanking}
import alleles.environment.parallel.{ParallelProgress, ParallelRanking}
import alleles.environment.sequential.{SeqProgress, SeqRanking}

import scala.collection.parallel.TaskSupport

/**
  * Fixed version of `Setting` with sequential operators and
  * additional extension methods.
  *
  * May be used as default implementation of `Ambience`.
  */
object GeneticAlgorithm extends Setting(SeqRanking, SeqProgress) {
  def par: Setting = new Setting(ParallelRanking, ParallelProgress)

  def par(taskSupport: TaskSupport): Setting =
    new Setting(
      new ConfigurableParRanking(taskSupport),
      new ConfigurableParProgress(taskSupport))

  def parFitness: Setting = new Setting(ParallelRanking, SeqProgress)
}
