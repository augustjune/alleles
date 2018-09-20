package genetic.operators.individual

import genetic.RRandom
import genetic.genotype.Mutation
import genetic.operators.IndividualMutation

case class RepetitiveMutation(individualChance: Double, repetitiveChance: Double) extends IndividualMutation {
  def apply[G: Mutation](g: G): G =
    if (RRandom.shot(individualChance)) modifyGenotype(g)
    else g

  protected def modifyGenotype[G: Mutation](g: G): G = {
    def mutateNext(genotype: G): G =
      if (RRandom.shot(repetitiveChance)) mutateNext(genotype)
      else genotype

    mutateNext(Mutation(g))
  }
}
