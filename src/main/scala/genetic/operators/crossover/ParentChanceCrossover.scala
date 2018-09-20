package genetic.operators.crossover

import cats.Semigroup
import cats.syntax.semigroup._
import genetic.RRandom
import genetic.collections.IterablePair
import genetic.operators.Crossover

case class ParentChanceCrossover(parentChance: Double) extends Crossover {
  def apply[G: Semigroup](parents: (G, G)): IterablePair[G] = parents match {
    case (p1, p2) =>
      if (RRandom.shot(parentChance)) IterablePair(p1, p2)
      else IterablePair(p1 |+| p2, p2 |+| p1)
  }
}

