package genetic.operators.selection

import genetic.operators.Selection
import genetic.Population
import genetic.genotype.Fitness

import scala.util.Random

case class Tournament[A: Fitness](size: Int) extends Selection[A] {
  def apply(population: Population[A]): Population[A] =
    for (_ <- population) yield
    Random.shuffle(population).take(size).minBy(Fitness(_))

  override def toString: String = s"Tournament selection with tournament size of $size"
}
