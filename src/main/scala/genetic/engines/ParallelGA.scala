package genetic.engines

import cats.{Id, Semigroup}
import genetic.genotype.{Fitness, Modification}
import genetic.{AlgoSettings, Population}

import scala.collection.parallel.immutable.ParVector

object ParallelGA extends SynchronousGA[Id] {

  protected def evolve[G: Fitness : Semigroup : Modification, B]
  (settings: AlgoSettings[G])
  (start: B, until: (B, Population[G]) => Boolean, click: B => B): Population[G] = {
    val initial = settings.initial
    val unitParVector = ParVector.fill(initial.size / 2)(())

    def loop(pop: Population[G], condition: B): Population[G] = {
      if (until(condition, pop)) loop(
        unitParVector
          .map(_ => settings.selection.single(pop))
          .flatMap(settings.crossover.single(_))
          .map(settings.mutation.single(_))
          .seq,
        click(condition)
      )
      else pop
    }

    loop(initial, start)
  }
}
