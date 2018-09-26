package genetic.engines.sync

import cats.Semigroup
import genetic.genotype.{Fitness, Modification}
import genetic.{OperatorSet, Population}

import scala.collection.parallel.immutable.ParVector

class ParallelGA extends SynchronousGA {

  protected def evolve[G: Fitness : Semigroup : Modification, B]
  (population: Population[G], operators: genetic.OperatorSet)
  (start: B, until: B => Boolean, click: B => B): Population[G] = {
    val parallelBase = ParVector.fill(population.size)(())

    def loop(generation: Population[G], condition: B): Population[G] =
      if (until(condition)) operators match {
        case OperatorSet(selection, crossover, mutation) =>
          loop(parallelBase
            .map(_ => selection.single(generation))
            .flatMap(crossover.single(_))
            .map(mutation.single(_))
            .seq, click(condition))
      } else generation

    loop(population, start)
  }
}
