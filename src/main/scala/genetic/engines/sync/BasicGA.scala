package genetic.engines.sync

import cats.Semigroup
import genetic.genotype.{Fitness, Modification}
import genetic.{OperatorSet, Population}

class BasicGA extends SynchronousGA {
  protected def evolve[G: Fitness : Semigroup : Modification, B]
  (population: Population[G], operators: OperatorSet)(start: B, until: B => Boolean, click: B => B): Population[G] = {
    def loop(generation: Population[G], condition: B): Population[G] =
      if (until(condition)) loop(operators.generation(generation), click(condition))
      else generation

    loop(population, start)
  }
}
