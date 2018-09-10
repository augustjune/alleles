package genetic

package object operators {

  /**
    * An operator performing changes on population
    * which lead to more adopted generation.
    */
  sealed trait GeneticOperator[A] extends (Population[A] => Population[A])

  /**
    * A genetic operator used to choose individual
    * genomes from a population for later breeding
    */
  trait Selection[A] extends GeneticOperator[A]

  /**
    * A genetic operator used to combine the genetic
    * information of two parents to generate new offspring.
    */
  trait Crossover[A] extends GeneticOperator[A]

  /**
    * A genetic operator used ot maintain genetic diversity from one
    * generation of a population of genetic algorithm chromosomes to the next.
    */
  trait Mutation[A] extends GeneticOperator[A]

  case class Same[A]() extends Selection[A] with Crossover[A] with Mutation[A] {
    def apply(pop: Population[A]): Population[A] = pop
  }
}
