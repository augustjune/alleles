package alleles.environment

import alleles.genotype.Fitness.Rated
import alleles.{Epoch, Population}

/**
  * Abstract representation of applying genetic operators to
  * the population of individuals with corresponding fitness values
  */
trait Progress[A] {
  def nextGeneration(ratedPop: Population[Rated[A]], operators: Epoch[A]): Population[A]
}
