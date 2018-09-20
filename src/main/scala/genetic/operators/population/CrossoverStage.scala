package genetic.operators.population

import cats.Semigroup
import genetic.Population
import genetic.operators.{IndividualCrossover, PopulationCrossover}


class CrossoverStage(individual: IndividualCrossover) extends PopulationCrossover {
  def apply[G: Semigroup](population: Population[(G, G)]): Population[G] =
    population.flatMap { case (p1, p2) => individual.apply(p1, p2) }
}
