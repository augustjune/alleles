package genetic.engines.sync

import genetic.genotype.{Fitness, Join, Modification}
import genetic.{OperatorSet, Population, PopulationExtension}

import scala.annotation.tailrec
import scala.collection.parallel.immutable.ParVector

/**
  * Parallel implementation of SynchronousGA
  */
object ParallelGA extends SynchronousGA {

  /**
    * Overridden implementation of parametrised loop with shared base parallel collection for each iteration
    */
  type ParPop[G] = ParVector[G]

  override protected def evolve[G: Fitness : Join : Modification, B]
  (population: Population[G], operators: genetic.OperatorSet)
  (start: B, until: B => Boolean, click: B => B): Population[G] = {
    val parallelBase = ParVector.fill(population.size / 2)(())

    @tailrec
    def loop(parPop: ParVector[G], condition: B): ParVector[G] = {
      val withFitness = parPop.map(g => g -> Fitness(g)).seq

      if (!until(condition)) parPop
      else operators match {
        case OperatorSet(selection, crossover, mutation) =>
          loop(parallelBase
            .map(_ => selection.single(withFitness))
            .flatMap(crossover.single(_))
            .map(mutation.single(_)), click(condition))
      }
    }

    loop(population.par, start).seq
  }

  /**
    * Single step of evolution of population with set of genetic operators
    */
  def evolutionStep[G: Fitness : Join : Modification](population: Population[G],
                                                      operators: OperatorSet): Population[G] =
    evolve[G, Boolean](population, operators)(true, identity, _ => false)
}
