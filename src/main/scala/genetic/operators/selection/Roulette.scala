package genetic.operators.selection

import genetic.operators.Selection
import genetic._

import scala.util.Random

case class Roulette[A: Fitness]() extends Selection[A] {
  def apply(population: Population[A]): Population[A] = {
    val largestFitness = population.map(Fitness(_)).max
    val range: Double = population.map(largestFitness - Fitness(_)).sum

    val rouletteSectors = population.map(x => x -> ((largestFitness - Fitness(x)) / range)).toMap

    def selectOne: A = Random.shotSeq(rouletteSectors.map(_.swap))

    def selectUntil(newPopSize: Int, acc: List[A]): List[A] =
      if (acc.length < newPopSize)
        selectUntil(newPopSize, selectOne :: acc)
      else acc

    selectUntil(population.length, Nil)
  }
}
