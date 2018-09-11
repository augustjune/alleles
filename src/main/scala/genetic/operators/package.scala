package genetic

package object operators {

  /**
    * An operator performing changes on population
    * which lead to more adopted generation.
    */
  sealed trait GeneticOperator[G] extends (Population[G] => Population[G])

  /**
    * G genetic operator used to choose individual
    * genomes from a population for later breeding
    */
  trait Selection[G] extends GeneticOperator[G]

  /**
    * G genetic operator used to combine the genetic
    * information of two parents to generate new offspring.
    */
  trait Crossover[G] extends GeneticOperator[G]

  /**
    * G genetic operator used ot maintain genetic diversity from one
    * generation of a population of genetic algorithm chromosomes to the next.
    */
  trait Mutation[G] extends GeneticOperator[G]

  case class Same[G]() extends Selection[G] with Crossover[G] with Mutation[G] {
    def apply(pop: Population[G]): Population[G] = pop
  }
}
