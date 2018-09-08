package genetic

package object operators {

  /**
    * An operator performing changes on population
    * which lead to more adopted generation.
    */
  sealed trait GeneticOperator extends (Population => Population)

  /**
    * A genetic operator used to choose individual
    * genomes from a population for later breeding
    */
  trait Selection extends GeneticOperator

  /**
    * A genetic operator used to combine the genetic
    * information of two parents to generate new offspring.
    */
  trait Crossover extends GeneticOperator

  /**
    * A genetic operator used ot maintain genetic diversity from one
    * generation of a population of genetic algorithm chromosomes to the next.
    */
  trait Mutation extends GeneticOperator

  object Same extends Selection with Crossover with Mutation {
    def apply(pop: Population): Population = pop
  }
}
