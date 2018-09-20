package genetic.engines

import cats.{Id, Semigroup}
import genetic.genotype.{Fitness, Modification}
import genetic.{AlgoSettings, GeneticAlgorithm, Population, PopulationExtension}

object BasicGA extends GeneticAlgorithm[Id] {

  def evolveUntilReached[G: Fitness: Semigroup: Modification](settings: AlgoSettings[G], fitnessThreshold: Int): Population[G] = {
    def loop(pop: Population[G]): Population[G] =
      if(Fitness(pop.best) > fitnessThreshold) settings.loop(pop)
      else pop

    loop(settings.initial)
  }

  protected def evolve[G: Fitness: Semigroup: Modification, B](settings: AlgoSettings[G])(start: B, until: B => Boolean, click: B => B): Population[G] = {
    def loop(pop: Population[G], condition: B): Population[G] =
      if (until(condition)) settings.loop(pop)
      else pop

    loop(settings.initial, start)
  }
}
