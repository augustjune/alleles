package genetic.engines

import genetic.{OperatorSet, Population}
import genetic.genotype._

trait EvolutionStrategy {
  def evolutionStep[G: Join : Modification](ratedPop: Population[(G, Double)],
                                            operators: OperatorSet): Population[G]
}
