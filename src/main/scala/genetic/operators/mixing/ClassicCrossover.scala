package genetic.operators.mixing

import genetic._
import genetic.operators.Mixing

import scala.util.Random

case class ClassicCrossover(parentChance: Double) extends Mixing {
  def apply(population: Population): Population = {
    def iter(pool: Population, acc: List[Permutation]): List[Permutation] = pool match {
      case Nil | List(_) => acc
      case p1 :: p2 :: tail =>
        if (Random.shot(parentChance)) iter(tail, p2 :: p1 :: acc)
        else iter(tail, p1.randomCrossOver(p2) :: p2.randomCrossOver(p1) :: acc)
    }

    iter(population, Nil)
  }

  override def toString: String = s"ClassicCrossover with parent stay chance $parentChance"
}
