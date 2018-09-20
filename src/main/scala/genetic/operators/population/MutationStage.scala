package genetic.operators.population

import genetic.Population
import genetic.genotype.Mutation
import genetic.operators.{IndividualMutation, PopulationMutation}

class MutationStage(individual: IndividualMutation) extends PopulationMutation {
  def apply[G: Mutation](pop: Population[G]): Population[G] = pop.map(x => individual.apply(x))
}
