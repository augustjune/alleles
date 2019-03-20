package genetic.engines

import genetic.{OperatorSet, Population}
import genetic.genotype._

trait Evolution {
  def nextGeneration[A: Join : Variation](ratedPop: Population[Rated[A]],
                                          operators: OperatorSet): Population[A]
}
