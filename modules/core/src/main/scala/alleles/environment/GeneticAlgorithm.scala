package alleles.environment

import alleles.environment.sequential.{SeqProgress, SeqRanking}
import alleles.genotype.{Fitness, Join, Variation}

/**
  * Fixed version of `Setting` with sequential operators and
  * additional extension methods.
  *
  * May be used as default implementation of `Ambience`.
  */
class GeneticAlgorithm[A: Fitness : Join : Variation] extends Setting[A](new SeqRanking, new SeqProgress)

object GeneticAlgorithm {
  def apply[A: Fitness : Join : Variation]: GeneticAlgorithm[A] = new GeneticAlgorithm[A]
}
