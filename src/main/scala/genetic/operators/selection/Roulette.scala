package genetic.operators.selection

import genetic.operators.Selection
import genetic._
import genetic.genotype.Fitness

import scala.util.Random

case class Roulette[A: Fitness]() extends Selection[A] {
  def apply(population: Population[A]): Population[A] = {
    val fitnesses = population.map(x => (x, Fitness(x)))
    val largestFitness = fitnesses.map(_._2).max

    val range = fitnesses.map(largestFitness - _._2).sum

    val sectors = fitnesses.map{ case (x, f) => (x, (largestFitness - f) * 1.0 / range)}

    for(_ <- population) yield Random.choose(sectors)
  }
}
