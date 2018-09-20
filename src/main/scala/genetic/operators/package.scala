package genetic

import cats.kernel.Semigroup
import genetic.collections.IterablePair
import genetic.genotype.{Fitness, Mutation}

package object operators {

  /**
    * An operator performing changes on population
    * which lead to more adopted generation.
    */
  sealed trait GeneticOperator[G] extends (Population[G] => Population[G])


  trait IndividualSelection {
    def apply[G: Fitness](population: Population[G]): (G, G)
  }

  trait IndividualCrossover {
    def apply[G: Semigroup](p1: G, p2: G): IterablePair[G]
  }

  trait IndividualMutation {
    def apply[G: Mutation](g: G): G
  }

  trait PopulationSelection {
    def apply[G: Fitness](population: Population[G]): Population[(G, G)]
  }

  trait PopulationCrossover {
    def apply[G: Semigroup](population: Population[(G, G)]): Population[G]
  }

  trait PopulationMutation {
    def apply[G: Mutation](population: Population[G]): Population[G]
  }
  /**
    * G genetic operator used to choose individual
    * genomes from a population for later breeding
    */

  /**
    * G genetic operator used to combine the genetic
    * information of two parents to generate new offspring.
    */

  /**
    * G genetic operator used ot maintain genetic diversity from one
    * generation of a population of genetic algorithm chromosomes to the next.
    */
}
