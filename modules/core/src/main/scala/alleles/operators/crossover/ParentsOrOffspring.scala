package alleles.operators.crossover

import alleles.toolset.{IterablePair, RRandom}
import alleles.genotype.Join
import alleles.genotype.syntax.JoinObj
import alleles.operators.Crossover

/**
  * Technique of combining genetic information of two parents into new offspring
  * with the probability of keeping the original genotypes in population without mixing
  *
  * @param parentChance Probability of original genotypes to be chosen to the next population
  */
class ParentsOrOffspring[A: Join](parentChance: Double) extends Crossover[A] {
  def pair(p1: A, p2: A): IterablePair[A] =
    if (RRandom.shot(parentChance)) new IterablePair(p1, p2)
    else p1 >< p2
}

class NoChildren[A] extends Crossover[A] {
  def pair(p1: A, p2: A): IterablePair[A] = new IterablePair[A](p1, p2)
}

class NoParents[A: Join] extends Crossover[A] {
  def pair(p1: A, p2: A): IterablePair[A] = p1 >< p2
}
