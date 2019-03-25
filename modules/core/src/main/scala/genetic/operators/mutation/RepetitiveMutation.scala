package genetic.operators.mutation

import genetic.genotype.Variation
import genetic.toolset.RRandom
import genetic.operators.Mutation
import genetic.genotype.syntax._

/**
  * Technique of mutation, which affects individual genotype with probability `individualChance` and
  * continues to recursively modify current individual with chance of each next modification `repetitiveChance`
  *
  * Probability of n-th modification is p(n) = individualChance * repetetiveChance^^(n)
  *
  * @param individualChance Probability of first modification
  * @param repetitiveChance Probability of each next modification
  */

class RepetitiveMutation(individualChance: Double, repetitiveChance: Double) extends Mutation {
  require(repetitiveChance < 1)

  def single[A: Variation](a: A): A =
    if (RRandom.shot(individualChance)) modifyIndividual(a)
    else a

  private def modifyIndividual[A: Variation](a: A): A = {
    def mutateNext(individual: A): A =
      if (RRandom.shot(repetitiveChance)) mutateNext(individual.modified)
      else individual

    mutateNext(a.modified)
  }
}
