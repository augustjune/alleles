package genetic

import genetic.toolset.IterablePair
import genetic.genotype.{Fitness, Join, Variation}

package object operators {
  /**
    * Genetic operator used to choose individual
    * genomes from a population for later breeding
    */
  trait Selection extends {
    type WithFitness[A] = (A, Double)

    def single[A](population: Population[WithFitness[A]]): (A, A)

    def generation[A](population: Population[WithFitness[A]]): Population[(A, A)]
  }

  /**
    * Genetic operator used to combine the genetic
    * information of two parents to generate new offspring.
    */
  trait Crossover {
    def single[A: Join](parents: (A, A)): IterablePair[A]

    def generation[A: Join](population: Population[(A, A)]): Population[A] = population.flatMap(single(_))
  }

  /**
    * Genetic operator used ot maintain genetic diversity from one
    * generation of a population of genetic algorithm chromosomes to the next.
    */
  trait Mutation {
    def single[A: Variation](individual: A): A

    def generation[A: Variation](population: Population[A]): Population[A] = population.map(single(_))
  }
}
