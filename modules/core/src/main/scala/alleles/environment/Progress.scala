package alleles.environment

import alleles.genotype.Fitness.Rated
import alleles.genotype._
import alleles.{Epoch, Population}
// ToDo - add documentation
trait Progress {
  def nextGeneration[A: Join : Variation](ratedPop: Population[Rated[A]],
                                          operators: Epoch[A]): Population[A]
}
