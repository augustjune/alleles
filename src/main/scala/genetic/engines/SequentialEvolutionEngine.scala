package genetic.engines

import genetic.genotype.{Fitness, Join, Modification}
import genetic.{OperatorSet, Population}

class SequentialEvolutionEngine extends EvolutionEngine {
  def evolutionStep[G: Join : Modification](scoredPop: Population[(G, Double)],
                                            operators: OperatorSet): Population[G] =
    operators.mutation.generation(
      operators.crossover.generation(
        operators.selection.generation(scoredPop)
      )
    )
}
