package alleles.environment

import alleles.genotype.Fitness.Rated
import alleles.genotype._
import alleles.{Epoch, Population}

/**
  * Abstract representation of applying genetic operators to
  * the population of individuals with corresponding fitness values
  */
trait Progress {
  def nextGeneration[A: Join : Variation](ratedPop: Population[Rated[A]],
                                          operators: Epoch[A]): Population[A]
}
