package genetic.operators.individual

import cats.Semigroup
import cats.syntax.semigroup._
import genetic.RRandom
import genetic.collections.IterablePair
import genetic.operators.IndividualCrossover

case class ParentChanceCrossover(parentChance: Double) extends IndividualCrossover {
  def apply[G: Semigroup](p1: G, p2: G): IterablePair[G] =
    if (RRandom.shot(parentChance)) IterablePair(p1, p2)
    else IterablePair(p1 |+| p2, p2 |+| p1)
}
