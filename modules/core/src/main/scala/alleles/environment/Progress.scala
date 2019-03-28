package alleles.environment

import alleles.genotype.Fitness.Rated
import alleles.genotype._
import alleles.{Epoch, Population}

trait Progress {
  def nextGeneration[A: Join : Variation](ratedPop: Population[Rated[A]],
                                          operators: Epoch): Population[A]
}
