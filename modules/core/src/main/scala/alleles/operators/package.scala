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

  /**
    * Genetic operator used ot maintain genetic diversity from one
    * generation of a population of genetic algorithm chromosomes to the next.
    */
  trait Mutation[A] {
    def single(individual: A): A

    def generation(population: Population[A]): Population[A] = population.map(single)
  }
}
