package genetic

import genetic.operators._

object AlgoSettings {
  /**
    * @param selection Selection function
    * @param operators Sequence of genetic operators applied after selection stage
    * @return Settings with established cycle of genetic operators
    */
  def apply[A](selection: Selection[A], operators: Seq[GeneticOperator[A]]): AlgoSettings[A] =
    AlgoSettings(operators.foldLeft(selection: Population[A] => Population[A])(_ andThen _))

  /**
    * @param selection Selection function
    * @param crossover Crossover function
    * @param mutation Mutation function
    * @return Settings with established cycle of genetic operators
    */
  def apply[A](selection: Selection[A] = Same(), crossover: Crossover[A] = Same(), mutation: Mutation[A] = Same()): AlgoSettings[A] =
    AlgoSettings(selection andThen crossover andThen mutation)
}

/**
  * A wrapper around the set of genetic operators used in genetic algorithm
  * @param cycle Set of genetic operators combined in a cycle
  */
case class AlgoSettings[A](cycle: Population[A] => Population[A])
