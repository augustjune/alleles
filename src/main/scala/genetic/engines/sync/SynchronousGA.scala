package genetic.engines.sync

import cats.Id
import genetic.genotype.{Fitness, Join, Modification, Scheme}
import genetic.{GeneticAlgorithmTemplate, OperatorSet, Population}

import scala.concurrent.duration.Duration

/**
  * Synchronous implementation of GeneticAlgorithm
  * Additionally allows to evolve population for certain duration
  */
trait SynchronousGA extends GeneticAlgorithmTemplate[Id] {
  /**
    * Evolves initial population with certain set of genetic operators for n iterations
    */
  def evolve[G: Fitness : Join : Modification](population: Population[G], operators: OperatorSet, iterations: Int): Population[G] =
    evolve[G, Long](population, operators)(0, _ < iterations, _ + 1)


  /**
    * Evolves new population of size `populationSize` with set of genetic operators for certain duration
    */
  def createAndEvolve[G: Fitness : Join : Modification: Scheme]
  (populationSize: Int, operators: OperatorSet, time: Duration): Population[G] =
    evolve(Scheme.make(populationSize), operators, time)

  /**
    * Evolves initial population with the set of genetic operators for certain duration
    */
  def evolve[G: Fitness : Join : Modification](population: Population[G], operators: OperatorSet, time: Duration): Population[G] = {
    val start = System.currentTimeMillis()
    evolve[G, Long](population, operators)(start, _ < start + time.toMillis, _ => System.currentTimeMillis())
  }


  protected def evolve[G: Fitness : Join : Modification, B]
  (population: Population[G], operators: OperatorSet)(start: B, until: B => Boolean, click: B => B): Population[G] = {
    def loop(pop: Population[G], condition: B): Population[G] =
      if (until(condition)) loop(evolutionStep(pop, operators), click(condition))
      else pop

    loop(population, start)
  }

  /**
    * Single step of evolution of population with set of genetic operators
    */
  def evolutionStep[G: Fitness : Join : Modification](population: Population[G], operators: OperatorSet): Population[G]
}
