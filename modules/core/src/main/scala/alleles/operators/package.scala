package alleles

import alleles.genotype.Fitness.Rated
import alleles.genotype.{Join, Variation}
import alleles.toolset.IterablePair

package object operators {
  /**
    * Genetic operator used to choose individual
    * genomes from a population for later breeding
    */
  trait Selection extends {
    def pair[A](population: Population[Rated[A]]): (A, A)

    def generation[A](population: Population[Rated[A]]): Population[(A, A)]
  }

}
