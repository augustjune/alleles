package alleles.operators

import alleles.Population
import alleles.genotype.Join
import alleles.toolset.{IterablePair, RRandom}
import alleles.genotype.syntax.JoinObj


/**
  * Genetic operator used to combine the genetic
  * information of two parents to generate new offspring.
  */
trait Crossover[A] {
  def pair(p1: A, p2: A): IterablePair[A]

  def generation(population: Population[(A, A)]): Population[A] = population.flatMap((pair _).tupled)
}

object Crossover {
  def parentsOrOffspring[A: Join](parentChance: Double): Crossover[A] =
    (p1: A, p2: A) =>
      if (RRandom.shot(parentChance)) new IterablePair(p1, p2)
      else p1 >< p2

  def noChildren[A]: Crossover[A] =
    (p1: A, p2: A) => new IterablePair[A](p1, p2)

  def noParents[A: Join]: Crossover[A] =
    (p1: A, p2: A) => p1 >< p2

}
