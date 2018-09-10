package genetic.operators.mixing

import genetic._
import genetic.operators.Crossover

import scala.util.Random

case class ClassicCrossover[A](parentChance: Double)(implicit c: Combinator[A]) extends Crossover[A] {
  def apply(population: Population[A]): Population[A] = {
    def iter(pool: Population[A], acc: List[A]): List[A] = pool match {
      case Nil | List(_) => acc
      case p1 :: p2 :: tail =>
        if (Random.shot(parentChance)) iter(tail, p2 :: p1 :: acc)
        else iter(tail, c.combine(p1, p2) :: c.combine(p2, p1) :: acc)
    }

    iter(population, Nil)
  }

  override def toString: String = s"ClassicCrossover with parent stay chance $parentChance"
}
