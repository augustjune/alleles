package genetic.engines

import cats.Semigroup
import genetic.genotype.{Fitness, Modification}
import genetic.{AlgoSettings, Population}

import scala.language.reflectiveCalls

object CountingGA extends SynchronousGA[({type T[A] = (Int, A)})#T] {

  protected def evolve[G: Fitness : Semigroup : Modification, B]
  (settings: AlgoSettings[G])
  (start: B, until: (B, Population[G]) => Boolean, click: B => B): (Int, Population[G]) = {
    var c = 0

    def loop(pop: Population[G], condition: B): Population[G] =
      if (until(condition, pop)) {
        c += 1
        loop(settings.loop(pop), click(condition))
      } else pop

    val resultPopulation = loop(settings.initial, start)
    (c, resultPopulation)
  }
}
