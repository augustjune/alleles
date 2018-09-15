package genetic.engines

import cats.data.Writer
import genetic.genotype.Fitness
import genetic.{AlgoSettings, Population, PopulationExtension}

import scala.language.reflectiveCalls

object CounterGA extends GeneticAlgorithm[({ type T[A] = (Int, A) })#T]{

  def evolveUntilReached[G: Fitness](settings: AlgoSettings[G], fitnessThreshold: Int): (Int, Population[G]) = {
    var c = 0
    def loop(pop: Population[G]): Population[G] =
      if (Fitness(pop.best) > fitnessThreshold) {
        c += 1
        loop(settings.cycle(pop))
      } else pop

    val resultPopulation = loop(settings.initialPopulation)
    (c, resultPopulation)
  }

  protected def evolve[G, B](settings: AlgoSettings[G])(start: B, until: B => Boolean, click: B => B): (Int, Population[G]) = {
    var c = 0
    def loop(pop: Population[G], condition: B): Population[G] =
      if (until(condition)) {
        c += 1
        loop(settings.cycle(pop), click(condition))
      } else pop

    val resultPopulation = loop(settings.initialPopulation, start)
    (c, resultPopulation)
  }
}
