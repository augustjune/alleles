package genetic.operators.mixing

import cats.Semigroup
import cats.syntax.semigroup._
import genetic._
import genetic.operators.Crossover

import scala.util.Random

case class ClassicCrossover[A: Semigroup](parentChance: Double) extends Crossover[A] {
  def apply(population: Population[A]): Population[A] = {
    def loop(pool: Population[A], acc: List[A]): List[A] = pool match {
      case Nil | List(_) => acc
      case p1 :: p2 :: tail =>
        if (Random.shot(parentChance)) loop(tail, p2 :: p1 :: acc)
        else loop(tail, (p1 |+| p2) :: (p2 |+| p1) :: acc)
    }

    loop(population, Nil)
  }

  override def toString: String = s"ClassicCrossover with parent stay chance $parentChance"
}
