package genetic.engines

import cats.Id
import genetic.genotype.Fitness
import genetic.{AlgoSettings, Population, PopulationExtension}

object ConcreteGA extends GeneticAlgorithm[Id] {

  def evolveUntilReached[G: Fitness](settings: AlgoSettings[G], fitnessThreshold: Int): Population[G] = {
    def loop(pop: Population[G]): Population[G] =
      if(Fitness(pop.best) > fitnessThreshold) loop(settings.cycle(pop))
      else pop

    loop(settings.initialPopulation)
  }

  protected def evolve[G, B](settings: AlgoSettings[G])(start: B, until: B => Boolean, click: B => B): Population[G] = {
    def loop(pop: Population[G], condition: B): Population[G] =
      if (until(condition)) loop(settings.cycle(pop), click(condition))
      else pop

    loop(settings.initialPopulation, start)
  }
}
