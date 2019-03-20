package genetic.operators.crossover

import genetic.toolset.{IterablePair, RRandom}
import genetic.genotype.Join
import genetic.genotype.syntax.JoinObj
import genetic.operators.Crossover

/**
  * Technique of combining genetic information of two parents into new offspring
  * with the probability of keeping the original genotypes in population without mixing
  *
  * @param parentChance Probability of original genotypes to be chosen to the next population
  */
case class ParentsOrOffspring(parentChance: Double) extends Crossover {
  def single[A: Join](parents: (A, A)): IterablePair[A] = parents match {
    case (p1, p2) =>
      if (RRandom.shot(parentChance)) IterablePair(p1, p2)
      else p1 >< p2
  }
}
