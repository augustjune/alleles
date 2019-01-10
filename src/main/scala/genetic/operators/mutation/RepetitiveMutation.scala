package genetic.operators.mutation

import genetic.genotype.Modification
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

  def single[G: Modification](g: G): G =
    if (RRandom.shot(individualChance)) modifyGenotype(g)
    else g

  protected def modifyGenotype[G: Modification](g: G): G = {
    def mutateNext(genotype: G): G =
      if (RRandom.shot(repetitiveChance)) mutateNext(genotype)
      else genotype

    mutateNext(Modification(g))
  }
}
