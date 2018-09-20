package genetic.operators.mutation

import genetic.genotype.Modification
import genetic.operators.Mutation
import genetic.{Population, RRandom}

case class RepetitiveMutation(individualChance: Double, repetitiveChance: Double) extends Mutation {
  def apply[G: Modification](g: G): G =
    if (RRandom.shot(individualChance)) modifyGenotype(g)
    else g

  protected def modifyGenotype[G: Modification](g: G): G = {
    def mutateNext(genotype: G): G =
      if (RRandom.shot(repetitiveChance)) mutateNext(genotype)
      else genotype

    mutateNext(Modification(g))
  }
}
