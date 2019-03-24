package genetic.engines

import genetic.genotype.Fitness.Rated
import genetic.genotype._
import genetic.{OperatorSet, Population}

trait Evolution {
  def nextGeneration[A: Join : Variation](ratedPop: Population[Rated[A]],
                                          operators: OperatorSet): Population[A]
}
