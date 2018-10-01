package genetic.engines.sync

import genetic.genotype.{Fitness, Join, Modification}
import genetic.{OperatorSet, Population}

/**
  * Sequential implementation of SynchronousGA
  */
object SequentialGA extends SynchronousGA {
  /**
    * Single step of evolution of population with set of genetic operators
    */
  def evolutionStep[G: Fitness : Join : Modification](population: Population[G], operators: OperatorSet): Population[G] =
    operators.generationCycle(population)
}
