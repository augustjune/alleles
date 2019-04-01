package alleles.stages

import alleles.Population
import alleles.genotype.Variation
import alleles.genotype.syntax._
import alleles.toolset.RRandom

/**
  * Genetic operator used ot maintain genetic diversity from one
  * generation of a population of genetic algorithm chromosomes to the next.
  */
trait MutationStrategy[A] {
  def single(individual: A): A

  def generation(population: Population[A]): Population[A] = population.map(single)
}

object MutationStrategy {

  def singleMutation[A: Variation](chance: Double): MutationStrategy[A] =
    (individual: A) =>
      if (RRandom.shot(chance)) individual.modified
      else individual

  /**
    * Technique of mutation, which affects individual genotype with probability `individualChance` and
    * continues to recursively modify current individual with chance of each next modification `repetitiveChance`
    *
    * Probability of n-th modification is p(n) = individualChance * repetitiveChance^^(n)
    *
    * @param individualChance Probability of first modification
    * @param repetitiveChance Probability of each next modification
    */
  def repetitiveMutation[A: Variation](individualChance: Double, repetitiveChance: Double): MutationStrategy[A] = new MutationStrategy[A] {
    def single(a: A): A =
      if (RRandom.shot(individualChance)) modifyIndividual(a)
      else a

    private def modifyIndividual(a: A): A = {
      def mutateNext(individual: A): A =
        if (RRandom.shot(repetitiveChance)) mutateNext(individual.modified)
        else individual

      mutateNext(a.modified)
    }
  }
}
