package genetic

import cats.kernel.Semigroup
import genetic.collections.IterablePair
import genetic.genotype.{Fitness, Modification}

package object operators {
  /**
    * Genetic operator used to choose individual
    * genomes from a population for later breeding
    */
  trait Selection extends {
    def apply[G: Fitness](population: Population[G]): (G, G)

    def expand[G: Fitness]: Population[G] => Population[(G, G)]
  }

  /**
    * Genetic operator used to combine the genetic
    * information of two parents to generate new offspring.
    */
  trait Crossover {
    def apply[G: Semigroup](parents: (G, G)): IterablePair[G]

    def expand[G: Semigroup]: Population[(G, G)] => Population[G] = _.flatMap(apply(_))
  }

  /**
    * Genetic operator used ot maintain genetic diversity from one
    * generation of a population of genetic algorithm chromosomes to the next.
    */
  trait Mutation {
    def apply[G: Modification](individual: G): G

    def expand[G: Modification]: Population[G] => Population[G] = _.map(apply(_))
  }
}
