package genetic.engines

import genetic.engines.parallel.{ParallelProgress, ParallelRanking}
import genetic.engines.parallel.configurable.{ConfigurableParProgress, ConfigurableParRanking}
import genetic.engines.sequential.{SeqProgress, SeqRanking}

import scala.collection.parallel.TaskSupport

object GeneticAlgorithm extends Setting(SeqRanking, SeqProgress) {
  def par: Setting = new Setting(ParallelRanking, ParallelProgress)

  def par(taskSupport: TaskSupport): Setting =
    new Setting(
      new ConfigurableParRanking(taskSupport),
      new ConfigurableParProgress(taskSupport))

  def parFitness: Setting = new Setting(ParallelRanking, SeqProgress)
}

