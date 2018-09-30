package genetic.engines.sync

import genetic.genotype.{Fitness, Join, Modification}
import genetic.{OperatorSet, Population}

object BasicGA extends SynchronousGA {
  def evolutionStep[G: Fitness : Join : Modification](population: Population[G],
                                                           operators: OperatorSet): Population[G] =
    operators.generation(population)
}
