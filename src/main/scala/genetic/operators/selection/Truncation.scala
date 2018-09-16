package genetic.operators.selection

import genetic.Population
import genetic.genotype.Fitness
import genetic.operators.Selection

/**
  * Selecting the best half, third or other proportion of the population
  */

object Truncation {
  def apply[G: Fitness](proportion: Double): Truncation[G] = new Truncation[G](proportion)
}

class Truncation[G: Fitness](proportion: Double) extends Selection[G] {
  /*
      Optimization point: do not sort rest of the population after reaching needed proportion
   */
  def apply(pop: Population[G]): Population[G] =
    pop.sortBy(Fitness(_)).take((pop.size * proportion).toInt)
}
