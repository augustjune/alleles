package genetic.engines.sync

import cats.Semigroup
import genetic.genotype.{Fitness, Modification}
import genetic.{OperatorSet, Population}

import scala.collection.parallel.immutable.ParVector

object ParallelGA extends SynchronousGA {

  override protected def evolve[G: Fitness : Semigroup : Modification, B]
  (population: Population[G], operators: genetic.OperatorSet)
  (start: B, until: B => Boolean, click: B => B): Population[G] = {
    val parallelBase = ParVector.fill(population.size / 2)(())

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

  def evolutionStep[G: Fitness : Semigroup : Modification](population: Population[G],
                                                           operators: OperatorSet): Population[G] =
    evolve[G, Boolean](population, operators)(true, identity, _ => false)
}
