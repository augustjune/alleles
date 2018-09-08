package genetic.operators.selection

import genetic.operators.Selection
import genetic.{Genotype, Population}

import scala.util.Random

case class Tournament(size: Int) extends Selection {
  def apply(population: Population): Population = {
    def takeBest(tour: Population): Genotype = tour.minBy(_.fitnessValue)

    def selectOne: Genotype =
      takeBest(Random.shuffle(population).take(size))

    def selectUntil(newPopSize: Int, acc: List[Genotype]): List[Genotype] =
      if (acc.length < newPopSize)
        selectUntil(newPopSize, selectOne :: acc)
      else acc

    selectUntil(population.length, Nil)
  }

  override def toString: String = s"Tournament selection with tournament size of $size"
}
