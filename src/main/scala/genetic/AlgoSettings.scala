package genetic

import genetic.genotype.Design
import genetic.operators._

object AlgoSettings {
  /**
    * @param selection Selection function
    * @param operators Sequence of genetic operators applied after selection stage
    * @return Settings with established cycle of genetic operators
    */
  def apply[G: Design](initPopSize: Int, selection: Selection[G], operators: Seq[GeneticOperator[G]]): AlgoSettings[G] =
    new AlgoSettings[G] {
      lazy val cycle: Population[G] => Population[G] =
        operators.foldLeft(selection: Population[G] => Population[G])(_ andThen _)

      def initialPopulation: Population[G] = Design.make(initPopSize)
    }

  /**
    * @param selection Selection function
    * @param crossover Crossover function
    * @param mutation Mutation function
    * @return Settings with established cycle of genetic operators
    */
  def apply[G: Design](initPopSize: Int, selection: Selection[G] = Same(), crossover: Crossover[G] = Same(), mutation: Mutation[G] = Same()): AlgoSettings[G] =
    new AlgoSettings[G] {
      lazy val cycle: Population[G] => Population[G] = selection andThen crossover andThen mutation

      def initialPopulation: Population[G] = Design.make(initPopSize)
    }
}

trait AlgoSettings[G] {
  def cycle: Population[G] => Population[G]
  def initialPopulation: Population[G]
}
