package genetic.operators.selection

import genetic.Population
import genetic.genotype.Fitness
import genetic.operators.Selection

import scala.util.Random

case class Tournament[G: Fitness](size: Int) extends Selection[G] {
  def apply(population: Population[G]): Population[G] =
    for (_ <- population) yield
    Random.shuffle(population).take(size).minBy(Fitness(_))

  override def toString: String = s"Tournament selection with tournament size of $size"
}
