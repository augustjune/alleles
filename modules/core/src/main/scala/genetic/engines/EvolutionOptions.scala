package genetic.engines

import genetic.{OperatorSet, Population}
import genetic.genotype.Scheme

trait EvolutionOptions[A] {
  def initialPopulation: Population[A]

  def operators: OperatorSet
}

object EvolutionOptions {
  def apply[A](initialPopulation: Population[A], operators: OperatorSet): EvolutionOptions[A] =
    new ConcreteEO(initialPopulation, operators)

  def apply[A: Scheme](populationSize: Int, operators: OperatorSet): EvolutionOptions[A] =
    new LazyEO(populationSize, operators)
}

private final class ConcreteEO[A](val initialPopulation: Population[A],
                                  val operators: OperatorSet) extends EvolutionOptions[A]

private final class LazyEO[A: Scheme](populationSize: Int, val operators: OperatorSet) extends EvolutionOptions[A] {
  def initialPopulation: Population[A] = Scheme.make(populationSize)
}
