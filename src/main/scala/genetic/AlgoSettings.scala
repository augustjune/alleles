package genetic

import genetic.genotype.Design
import genetic.operators._

object AlgoSettings {
  /**
    * @param selection Selection function
    * @param operators Sequence of genetic operators applied after selection stage
    * @return Settings with established cycle of genetic operators
    */
  def apply[A: Design](initPopSize: Int, selection: Selection[A], operators: Seq[GeneticOperator[A]]): AlgoSettings[A] =
    new AlgoSettings[A] {
      lazy val cycle: Population[A] => Population[A] =
        operators.foldLeft(selection: Population[A] => Population[A])(_ andThen _)

      def initialPopulation: Population[A] = Design.make(initPopSize)
    }

  /**
    * @param selection Selection function
    * @param crossover Crossover function
    * @param mutation Mutation function
    * @return Settings with established cycle of genetic operators
    */
  def apply[A: Design](initPopSize: Int, selection: Selection[A] = Same(), crossover: Crossover[A] = Same(), mutation: Mutation[A] = Same()): AlgoSettings[A] =
    new AlgoSettings[A] {
      lazy val cycle: Population[A] => Population[A] = selection andThen crossover andThen mutation

      def initialPopulation: Population[A] = Design.make(initPopSize)
    }
}

/**
  * A wrapper around the set of genetic operators used in genetic algorithm
  */
trait AlgoSettings[A] {
  def cycle: Population[A] => Population[A]
  def initialPopulation: Population[A]
}
