package genetic

import genetic.operators._

object AlgoSettings {
  /**
    * @param selection Selection function
    * @param operators Sequence of genetic operators applied after selection stage
    * @return Settings with established cycle of genetic operators
    */
  def apply(selection: Selection, operators: Seq[GeneticOperator]): AlgoSettings =
    AlgoSettings(operators.foldLeft(selection: Population => Population)(_ andThen _))

  /**
    * @param selection Selection function
    * @param crossover Crossover function
    * @param mutation Mutation function
    * @return Settings with established cycle of genetic operators
    */
  def apply(selection: Selection = Same, crossover: Crossover = Same, mutation: Mutation = Same): AlgoSettings =
    AlgoSettings(selection andThen crossover andThen mutation)
}

/**
  * A wrapper around the set of genetic operators used in genetic algorithm
  * @param cycle Set of genetic operators combined in a cycle
  */
case class AlgoSettings(cycle: Population => Population)
