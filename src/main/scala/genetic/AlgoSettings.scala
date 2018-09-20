package genetic

import genetic.genotype.Scheme
import genetic.operators._

object AlgoSettings {
  /**
    * @param selection Selection function
    * @param crossover Crossover function
    * @param mutation Mutation function
    * @return Settings with established cycle of genetic operators
    */
  def apply[G: Scheme](initPopSize: Int,
                       selection: SelectionStage[G],
                       crossover: CrossoverStage[G],
                       mutation: MutationStage[G]): AlgoSettings[G] =
    new AlgoSettings[G] {
      lazy val cycle: Population[G] => Population[G] = selection andThen crossover andThen mutation

      def initialPopulation: Population[G] = Scheme.make(initPopSize)
    }
}

trait AlgoSettings[G] {
  def cycle: Population[G] => Population[G]
  def initialPopulation: Population[G]
}
