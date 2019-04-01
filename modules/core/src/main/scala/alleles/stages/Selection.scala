package alleles.stages

import alleles.Population
import alleles.genotype.Fitness.Rated

/**
  * Genetic operator used to choose individual
  * genomes from a population for later breeding
  */
trait Selection {
  def pair[A](population: Population[Rated[A]]): (A, A)

  def generation[A](population: Population[Rated[A]]): Population[(A, A)]
}
