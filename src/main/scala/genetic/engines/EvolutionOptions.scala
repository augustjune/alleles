package genetic.engines

import genetic.{OperatorSet, Population}
import genetic.genotype.Scheme

trait EvolutionOptions[G] {
  def initialPopulation: Population[G]

  def operators: OperatorSet
}

object EvolutionOptions {
  def apply[G](initialPopulation: Population[G], operators: OperatorSet): EvolutionOptions[G] =
    new ConcreteEO(initialPopulation, operators)

  def apply[G: Scheme](populationSize: Int, operators: OperatorSet): EvolutionOptions[G] =
    new LazyEO(populationSize, operators)
}

private final class ConcreteEO[G](val initialPopulation: Population[G],
                                  val operators: OperatorSet) extends EvolutionOptions[G]

private final class LazyEO[G: Scheme](populationSize: Int, val operators: OperatorSet) extends EvolutionOptions[G] {
  def initialPopulation: Population[G] = Scheme.make(populationSize)
}
