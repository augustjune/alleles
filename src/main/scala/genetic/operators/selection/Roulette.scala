package genetic.operators.selection

import genetic.operators.Selection
import genetic._
import genetic.genotype.Fitness
import genetic.genotype.syntax.FitnessObj

import scala.util.Random

case class Roulette[A: Fitness]() extends Selection[A] {
  def apply(population: Population[A]): Population[A] = {
    val fitnesses = population.map(x => x -> x.fitness)
    val largestFitness = fitnesses.map(_._2).max

    val sectors = fitnesses.map { case (x, f) => x -> (largestFitness - f)}

    for(_ <- population) yield Random.chooseByPriorities(sectors)
  }
}
