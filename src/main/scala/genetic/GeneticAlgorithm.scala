package genetic

import scala.concurrent.duration.Duration

object GeneticAlgorithm {
  /**
    * Evolves initial population with genetic operators for a finite number of iterations
    * @param settings Set of genetic operators which define iterative cycle
    * @param iterations Number of iterations
    * @return Evolved population
    */
  def evolve[G](settings: AlgoSettings[G], iterations: Int): Population[G] =
    evolve[G, Int](settings)(0, _ < iterations, _ + 1)

  /**
    * Evolves initial population with genetic operators for a provided time
    * Warning: is nondeterministic because of varying time of performance of a certain operation by CPU,
    *         thus can not be exactly reproduced with ReusableRandom
    * @param settings Set of genetic operators which define iterative cycle
    * @param duration Duration of evolving
    * @return Evolved population
    */
  def evolve[G](settings: AlgoSettings[G], duration: Duration): Population[G] = {
    val start = System.currentTimeMillis()
    evolve[G, Long](settings)(start, _ < start + duration.toMillis, _ => System.currentTimeMillis())
  }

  protected def evolve[G, B](settings: AlgoSettings[G])(start: B, until: B => Boolean, click: B => B): Population[G] = {
    def loop(pop: Population[G], condition: B): Population[G] =
      if (until(condition)) loop(settings.cycle(pop), click(condition))
      else pop

    loop(settings.initialPopulation, start)
  }
}
