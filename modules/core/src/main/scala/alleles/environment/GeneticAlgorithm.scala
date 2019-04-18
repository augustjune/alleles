package alleles.environment

import alleles.environment.parallel.configurable.{ConfigurableParProgress, ConfigurableParRanking}
import alleles.environment.parallel.{ParallelProgress, ParallelRanking}
import alleles.environment.sequential.{SeqProgress, SeqRanking}
import alleles.genotype.{Fitness, Join, Variation}

import scala.collection.parallel.TaskSupport

/**
  * Fixed version of `Setting` with sequential operators and
  * additional extension methods.
  *
  * May be used as default implementation of `Ambience`.
  */
class GeneticAlgorithm[A: Fitness : Join : Variation] extends Setting[A](new SeqRanking[A], SeqProgress) {
  def par: Setting[A] = new Setting(new ParallelRanking, ParallelProgress)

  def par(taskSupport: TaskSupport): Setting[A] =
    new Setting(
      new ConfigurableParRanking(taskSupport),
      new ConfigurableParProgress(taskSupport))

  def parFitness: Setting[A] = new Setting(new ParallelRanking, SeqProgress)
}

object GeneticAlgorithm {
  def apply[A: Fitness : Join : Variation]: GeneticAlgorithm[A] = new GeneticAlgorithm[A]
}
