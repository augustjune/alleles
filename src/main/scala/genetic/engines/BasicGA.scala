package genetic.engines

import cats.{Id, Semigroup}
import genetic.genotype.{Fitness, Modification}
import genetic.{AlgoSettings, Population}

object BasicGA extends SynchronousGA[Id] {

  protected def evolve[G: Fitness: Semigroup: Modification, B](settings: AlgoSettings[G])(start: B, until: (B, Population[G]) => Boolean, click: B => B): Population[G] = {
    def loop(pop: Population[G], condition: B): Population[G] =
      if (until(condition, pop)) settings.loop(pop)
      else pop

    loop(settings.initial, start)
  }
}
