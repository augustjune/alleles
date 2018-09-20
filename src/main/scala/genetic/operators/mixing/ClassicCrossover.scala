package genetic.operators.mixing

import cats.Semigroup
import cats.syntax.semigroup._
import genetic._
import genetic.operators.CrossoverStage

object ClassicCrossover {
  def apply[G: Semigroup](parentChance: Double): ClassicCrossover[G] = new ClassicCrossover(parentChance)
}

class ClassicCrossover[G: Semigroup](parentChance: Double) extends CrossoverStage[G] {
  def apply(population: Population[G]): Population[G] = {
    def loop(pool: Population[G], acc: List[G]): List[G] = pool match {
      case Nil | List(_) => acc
      case p1 :: p2 :: tail =>
        if (RRandom.shot(parentChance)) loop(tail, p2 :: p1 :: acc)
        else loop(tail, (p1 |+| p2) :: (p2 |+| p1) :: acc)
    }

    loop(population, Nil)
  }

  override def toString: String = s"ClassicCrossover with parent stay chance $parentChance"
}
