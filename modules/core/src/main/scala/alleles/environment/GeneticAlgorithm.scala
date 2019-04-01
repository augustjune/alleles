package alleles.environment

import alleles.environment.parallel.configurable.{ConfigurableParProgress, ConfigurableParRanking}
import alleles.environment.parallel.{ParallelProgress, ParallelRanking}
import alleles.environment.sequential.{SeqProgress, SeqRanking}

import scala.collection.parallel.TaskSupport
// ToDo - add documentation
object GeneticAlgorithm extends Setting(SeqRanking, SeqProgress) {
  def par: Setting = new Setting(ParallelRanking, ParallelProgress)

  def par(taskSupport: TaskSupport): Setting =
    new Setting(
      new ConfigurableParRanking(taskSupport),
      new ConfigurableParProgress(taskSupport))

  def parFitness: Setting = new Setting(ParallelRanking, SeqProgress)
}

