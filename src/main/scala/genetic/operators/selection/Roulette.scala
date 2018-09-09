package genetic.operators.selection

import genetic.operators.Selection
import genetic._

import scala.util.Random

object Roulette extends Selection {
  def apply(population: Population): Population = {
    val largestFitness = population.map(_.fitnessValue).max
    val range: Double = population.map(largestFitness - _.fitnessValue).sum

    val rouletteSectors = population.map(x => x -> ((largestFitness - x.fitnessValue) / range)).toMap

    def selectOne: Genotype = population(Random.shotSeq(rouletteSectors.map(_.swap)))

    def selectUntil(newPopSize: Int, acc: List[Genotype]): List[Genotype] =
      if (acc.length < newPopSize)
        selectUntil(newPopSize, selectOne :: acc)
      else acc

    selectUntil(population.length, Nil)
  }
}
