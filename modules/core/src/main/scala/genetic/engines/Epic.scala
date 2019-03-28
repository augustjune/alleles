package genetic.engines

import genetic.{Epoch, Population}
import genetic.genotype.Scheme

trait Epic[A] {
  def initialPopulation: Population[A]

  def operators: Epoch
}

object Epic {
  def apply[A](initialPopulation: Population[A], operators: Epoch): Epic[A] =
    new ConcreteEO(initialPopulation, operators)

  def apply[A: Scheme](populationSize: Int, operators: Epoch): Epic[A] =
    new LazyEO(populationSize, operators)
}

private final class ConcreteEO[A](val initialPopulation: Population[A],
                                  val operators: Epoch) extends Epic[A]

private final class LazyEO[A: Scheme](populationSize: Int, val operators: Epoch) extends Epic[A] {
  def initialPopulation: Population[A] = Scheme.make(populationSize)
}
