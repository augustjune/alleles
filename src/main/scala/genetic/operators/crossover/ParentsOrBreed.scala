package genetic.operators.crossover

import genetic.genotype.syntax.JoinObj
import genetic.RRandom
import genetic.collections.IterablePair
import genetic.genotype.Join
import genetic.operators.Crossover

case class ParentsOrBreed(parentChance: Double) extends Crossover {
  def single[G: Join](parents: (G, G)): IterablePair[G] = parents match {
    case (p1, p2) =>
      if (RRandom.shot(parentChance)) IterablePair(p1, p2)
      else p1 >< p2
  }
}
