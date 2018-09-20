package genetic

import genetic.collections.IterablePair

package object operators {

  /**
    * An operator performing changes on population
    * which lead to more adopted generation.
    */
  sealed trait GeneticOperator[G] extends (Population[G] => Population[G])


  sealed trait Selection[G] extends (Population[G] => (G, G))
  sealed trait Crossover[G] extends ((G, G) => G)
  /**
    * G genetic operator used to choose individual
    * genomes from a population for later breeding
    */
  trait SelectionStage[G] extends (Population[G] => Population[IterablePair[G]])

  /**
    * G genetic operator used to combine the genetic
    * information of two parents to generate new offspring.
    */
  trait CrossoverStage[G] extends (Population[IterablePair[G]] => Population[G])

  /**
    * G genetic operator used ot maintain genetic diversity from one
    * generation of a population of genetic algorithm chromosomes to the next.
    */
  trait MutationStage[G] extends (Population[G] => Population[G])
}
