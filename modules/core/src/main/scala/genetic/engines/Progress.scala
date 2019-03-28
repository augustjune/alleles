package genetic.engines

import genetic.genotype.Fitness.Rated
import genetic.genotype._
import genetic.{Epoch, Population}

trait Progress {
  def nextGeneration[A: Join : Variation](ratedPop: Population[Rated[A]],
                                          operators: Epoch): Population[A]
}
