package genetic

import cats.Semigroup
import genetic.genotype.{Fitness, Modification}
import genetic.operators.{Crossover, Mutation, Selection}

case class AlgoSettings[G](initial: Population[G], selection: Selection, crossover: Crossover, mutation: Mutation) {
  def loop(population: Population[G]) (implicit f: Fitness[G], s: Semigroup[G], m: Modification[G]): Population[G] =
    mutation.generation(crossover.generation(selection.generation(population)))
}
