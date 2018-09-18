package genetic.operators.mutation

import genetic._
import genetic.genotype.RandomChange
import genetic.operators.Mutation

object GenotypeMutation {
  def apply[G: RandomChange](individualChance: Double, repetitiveChance: Double = 0): GenotypeMutation[G] =
    new GenotypeMutation(individualChance, repetitiveChance)
}

/**
  * Mutation technique, which influences each individual of population with probability `individualChance`
  * one or more times, where probability of each next mutation occurrence is `repetativeChance`
  *
  * @param individualChance Chance of instance to be modified by mutation
  * @param repetitiveChance Probability of repetitive mutation of the same instance
  */
class GenotypeMutation[G: RandomChange](individualChance: Double, repetitiveChance: Double) extends Mutation[G] {
  def apply(pop: Population[G]): Population[G] = for (g <- pop) yield
    if (RRandom.shot(individualChance)) modifyGenotype(g) else g

  protected def modifyGenotype(g: G): G = {
    def mutateNext(genotype: G): G =
      if (RRandom.shot(repetitiveChance)) mutateNext(genotype)
      else genotype

    mutateNext(RandomChange(g))
  }

  override def toString: String = s"GenotypeMutation with single genotype mutation chance $individualChance and mutation complexity $repetitiveChance"
}
