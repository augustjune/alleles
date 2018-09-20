package genetic.operators.population

import genetic.Population
import genetic.genotype.Fitness
import genetic.operators.{IndividualSelection, PopulationSelection}

class SelectionStage(individual: IndividualSelection) extends PopulationSelection {
  def apply[G: Fitness](population: Population[G]): Population[(G, G)] =
    for (_ <- (1 to population.length / 2).toList) yield individual(population)
}
