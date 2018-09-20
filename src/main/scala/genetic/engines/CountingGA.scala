package genetic.engines

import cats.Semigroup
import cats.data.Writer
import genetic.genotype.{Fitness, Modification}
import genetic.{AlgoSettings, GeneticAlgorithm, Population, PopulationExtension}

import scala.language.reflectiveCalls

object CountingGA extends GeneticAlgorithm[({ type T[A] = (Int, A) })#T]{

  def evolveUntilReached[G: Fitness: Semigroup: Modification](settings: AlgoSettings[G], fitnessThreshold: Int): (Int, Population[G]) = {
    var c = 0
    def loop(pop: Population[G]): Population[G] =
      if (Fitness(pop.best) > fitnessThreshold) {
        c += 1
        loop(settings.loop(pop))
      } else pop

    val resultPopulation = loop(settings.initial)
    (c, resultPopulation)
  }

  protected def evolve[G: Fitness: Semigroup: Modification, B](settings: AlgoSettings[G])(start: B, until: B => Boolean, click: B => B): (Int, Population[G]) = {
    var c = 0
    def loop(pop: Population[G], condition: B): Population[G] =
      if (until(condition)) {
        c += 1
        loop(settings.loop(pop), click(condition))
      } else pop

    val resultPopulation = loop(settings.initial, start)
    (c, resultPopulation)
  }
}
