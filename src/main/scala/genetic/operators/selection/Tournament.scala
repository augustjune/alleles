package genetic.operators.selection

import genetic.{Population, RRandom}
import genetic.genotype.Fitness
import genetic.operators.Selection

object Tournament {
  def apply[G: Fitness](size: Int): Tournament[G] = new Tournament(size)
}

class Tournament[G: Fitness](size: Int) extends Selection[G] {
  def apply(population: Population[G]): Population[G] =
    for (_ <- population) yield
    RRandom.shuffle(population).take(size).minBy(Fitness(_))

  override def toString: String = s"Tournament selection with tournament size of $size"
}
