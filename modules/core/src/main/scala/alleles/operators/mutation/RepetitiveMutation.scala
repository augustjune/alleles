package alleles.operators.mutation

import alleles.genotype.Variation
import alleles.toolset.RRandom
import alleles.operators.Mutation
import alleles.genotype.syntax._

/**
  * Technique of mutation, which affects individual genotype with probability `individualChance` and
  * continues to recursively modify current individual with chance of each next modification `repetitiveChance`
  *
  * Probability of n-th modification is p(n) = individualChance * repetitiveChance^^(n)
  *
  * @param individualChance Probability of first modification
  * @param repetitiveChance Probability of each next modification
  */

class RepetitiveMutation[A: Variation](individualChance: Double, repetitiveChance: Double) extends Mutation[A] {
  require(repetitiveChance < 1)

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
