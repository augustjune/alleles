package genetic

import genetic.toolset.IterablePair
import genetic.genotype.{Fitness, Join, Modification}

package object operators {
  /**
    * Genetic operator used to choose individual
    * genomes from a population for later breeding
    */
  trait Selection extends {
    type WithFitness[A] = (A, Double)

    def single[G](population: Population[WithFitness[G]]): (G, G)

    def generation[G](population: Population[WithFitness[G]]): Population[(G, G)]
  }

  /**
    * Genetic operator used to combine the genetic
    * information of two parents to generate new offspring.
    */
  trait Crossover {
    def single[G: Join](parents: (G, G)): IterablePair[G]

    def generation[G: Join](population: Population[(G, G)]): Population[G] = population.flatMap(single(_))
  }

  /**
    * Genetic operator used ot maintain genetic diversity from one
    * generation of a population of genetic algorithm chromosomes to the next.
    */
  trait Mutation {
    def single[G: Modification](individual: G): G

    def generation[G: Modification](population: Population[G]): Population[G] = population.map(single(_))
  }
}
