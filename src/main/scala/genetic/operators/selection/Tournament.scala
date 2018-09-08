package genetic.operators.selection

import genetic.operators.Selection
import genetic.{Permutation, Population}

import scala.util.Random

case class Tournament(size: Int) extends Selection {
  def apply(population: Population): Population = {
    def takeBest(tour: Population): Permutation = tour.minBy(_.fitnessValue)

    def selectOne: Permutation =
      takeBest(Random.shuffle(population).take(size))

    def selectUntil(newPopSize: Int, acc: List[Permutation]): List[Permutation] =
      if (acc.length < newPopSize)
        selectUntil(newPopSize, selectOne :: acc)
      else acc

    selectUntil(population.length, Nil)
  }

  override def toString: String = s"Tournament selection with tournament size of $size"
}
