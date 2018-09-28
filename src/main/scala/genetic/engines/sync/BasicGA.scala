package genetic.engines.sync

import cats.Semigroup
import genetic.genotype.{Fitness, Modification}
import genetic.{OperatorSet, Population}

object BasicGA extends SynchronousGA {
  def evolutionStep[G: Fitness : Semigroup : Modification](population: Population[G],
                                                           operators: OperatorSet): Population[G] =
    operators.generation(population)
}
