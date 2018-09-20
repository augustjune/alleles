package genetic.operators.population

import genetic.{Population, RRandom}
import genetic.genotype.Fitness
import genetic.operators.PopulationSelection

/**
  * Selecting the best half, third or other proportion of the population
  */

object Truncation {
  def apply(proportion: Double): Truncation = new Truncation(proportion)
}

class Truncation(proportion: Double) extends PopulationSelection {
  /*
      Optimization point: do not sort rest of the population after reaching needed proportion
      One more: DELETE EVERYTHING FOR FUCKS SAKE
   */
  def apply[G: Fitness](pop: Population[G]): Population[(G, G)] = {
    val selection = pop.sortBy(Fitness(_)).take((pop.size * proportion).toInt)
    val (bef, after) = RRandom.shuffle(selection).splitAt(selection.size / 2)
    bef.zip(after)
  }
}
