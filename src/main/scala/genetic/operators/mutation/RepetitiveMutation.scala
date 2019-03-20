package genetic.operators.mutation

import genetic.genotype.Variation
import genetic.toolset.RRandom
import genetic.operators.Mutation

/**
  * Technique of mutation, which affects individual genotype with probability `individualChance` and
  * continues to recursively modify current individual with chance of each next modification `repetitiveChance`
  *
  * Probability of n-th modification is p(n) = individualChance * repetetiveChance^^(n)
  *
  * @param individualChance Probability of first modification
  * @param repetitiveChance Probability of each next modification
  */

case class RepetitiveMutation(individualChance: Double, repetitiveChance: Double) extends Mutation {
  require(repetitiveChance < 1)

  def single[A: Variation](a: A): A =
    if (RRandom.shot(individualChance)) modifyGenotype(a)
    else a

  protected def modifyGenotype[A: Variation](a: A): A = {
    def mutateNext(genotype: A): A =
      if (RRandom.shot(repetitiveChance)) mutateNext(genotype)
      else genotype

    mutateNext(Variation(a))
  }
}
