package genetic.operators.selection

import genetic.genotype.Fitness
import genetic.operators.Selection
import genetic.{Population, RRandom}

/**
  * Selecting the best half, third or other proportion of the population
  */

object Truncation {
  def apply(proportion: Double): Truncation = new Truncation(proportion)
}

class Truncation(proportion: Double) extends Selection {
  def single[G: Fitness](pop: Population[G]): (G, G) =
    pop.sortBy(Fitness(_)).take(2) match {
      case List(x, y) => (x, y)
    }

  def generation[G: Fitness](pop: Population[G]): Population[(G, G)] = {
    val selection = pop.sortBy(Fitness(_)).take((pop.size * proportion).toInt)
    val (bef, after) = RRandom.shuffle(selection).splitAt(selection.size / 2)
    bef.zip(after)
  }

}
