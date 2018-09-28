package genetic.engines.sync

import cats.{Id, Semigroup}
import genetic.genotype.{Fitness, Modification, Scheme}
import genetic.{GeneticAlgorithmTemplate, OperatorSet, Population}

import scala.concurrent.duration.Duration

trait SynchronousGA extends GeneticAlgorithmTemplate[Id] {

  def evolve[G: Fitness : Semigroup : Modification](population: Population[G], operators: OperatorSet, iterations: Int): Population[G] =
    evolve[G, Long](population, operators)(0, _ < iterations, _ + 1)

  def createAndEvolve[G: Fitness : Semigroup : Modification: Scheme]
  (populationSize: Int, operators: OperatorSet, time: Duration): Population[G] =
    evolve(Scheme.make(populationSize), operators, time)


  def evolve[G: Fitness : Semigroup : Modification](population: Population[G], operators: OperatorSet, time: Duration): Population[G] = {
    val start = System.currentTimeMillis()
    evolve[G, Long](population, operators)(start, _ < start + time.toMillis, _ => System.currentTimeMillis())
  }


  protected def evolve[G: Fitness : Semigroup : Modification, B]
  (population: Population[G], operators: OperatorSet)(start: B, until: B => Boolean, click: B => B): Population[G] = {
    def loop(pop: Population[G], condition: B): Population[G] =
      if (until(condition)) loop(evolutionStep(pop, operators), click(condition))
      else pop

    loop(population, start)
  }

  def evolutionStep[G: Fitness : Semigroup : Modification](population: Population[G], operators: OperatorSet): Population[G]
}
