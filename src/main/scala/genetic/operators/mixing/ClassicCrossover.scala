package genetic.operators.mixing

import cats.Semigroup
import cats.syntax.semigroup._
import genetic._
import genetic.operators.Crossover

import scala.util.Random

case class ClassicCrossover[A: Semigroup](parentChance: Double) extends Crossover[A] {
  def apply(population: Population[A]): Population[A] = {
    def iter(pool: Population[A], acc: List[A]): List[A] = pool match {
      case Nil | List(_) => acc
      case p1 :: p2 :: tail =>
        if (Random.shot(parentChance)) iter(tail, p2 :: p1 :: acc)
        else iter(tail, (p1 |+| p2) :: (p2 |+| p1) :: acc)
    }

    iter(population, Nil)
  }

  override def toString: String = s"ClassicCrossover with parent stay chance $parentChance"
}
