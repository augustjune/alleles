package genetic.operators.selection

import genetic.operators.Selection
import genetic.{Fitness, Population}

import scala.util.Random

case class Tournament[A](size: Int)(implicit f: Fitness[A]) extends Selection[A] {
  def apply(population: Population[A]): Population[A] = {
    def takeBest(tour: Population[A]): A = tour.minBy(f.value)

    def selectOne: A =
      takeBest(Random.shuffle(population).take(size))

    def selectUntil(newPopSize: Int, acc: List[A]): List[A] =
      if (acc.length < newPopSize)
        selectUntil(newPopSize, selectOne :: acc)
      else acc

    selectUntil(population.length, Nil)
  }

  override def toString: String = s"Tournament selection with tournament size of $size"
}
