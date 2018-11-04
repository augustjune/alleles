package genetic.engines

import genetic.genotype.{Fitness, Join, Modification}
import genetic.{OperatorSet, Population}

class SequentialEvolutionEngine extends EvolutionEngine {

  def evalFitnesses[G: Fitness](population: Population[G]): Population[(G, Double)] =
    population.map(g => g -> Fitness(g))

  def evolutionStep[G: Join : Modification](scoredPop: Population[(G, Double)],
                                            operators: OperatorSet): Population[G] =
    operators.mutation.generation(
      operators.crossover.generation(
        operators.selection.generation(scoredPop)
      )
    )
}
