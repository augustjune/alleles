package genetic.operators.selection

import genetic.operators.Selection
import genetic._
import genetic.genotype.Fitness


object Roulette {
  def apply[G: Fitness]: Roulette[G] = new Roulette()
}

class Roulette[G: Fitness] extends Selection[G] {
  def apply(population: Population[G]): Population[G] = {
    val fitnesses = population.map(g => g -> Fitness(g))
    val largestFitness = fitnesses.map(_._2).max

    val sectors = fitnesses.map { case (g, f) => g -> (largestFitness - f)}

    for(_ <- population) yield RRandom.chooseByPriorities(sectors)
  }
}
