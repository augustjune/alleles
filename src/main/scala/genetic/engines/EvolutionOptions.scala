package genetic.engines

import genetic.{OperatorSet, Population}
import genetic.genotype.Scheme

trait EvolutionOptions[G] {
  def initialPopulation: Population[G]

  def operators: OperatorSet
}

object EvolutionOptions {
  def apply[G](initialPopulation: Population[G], operators: OperatorSet): EvolutionOptions[G] =
    ConcreteEO(initialPopulation, operators)

  def apply[G: Scheme](populationSize: Int, operators: OperatorSet): EvolutionOptions[G] =
    LazyEO(populationSize, operators)
}

private final case class ConcreteEO[G](initialPopulation: Population[G],
                                       operators: OperatorSet) extends EvolutionOptions[G]

private final case class LazyEO[G: Scheme](populationSize: Int, operators: OperatorSet) extends EvolutionOptions[G] {
  def initialPopulation: Population[G] = Scheme.make(populationSize)
}

