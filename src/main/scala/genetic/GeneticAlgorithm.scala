package genetic

import scala.concurrent.duration.Duration

object GeneticAlgorithm {
  /**
    * Evolves initial population with genetic operators for a finite number of iterations
    * @param settings Set of genetic operators which define iterative cycle
    * @param times Number of iteration
    * @return Evolved population
    */
  def iterate[G](settings: AlgoSettings[G], times: Int): Population[G] =
    iterate[G, Int](settings)(0, _ < times, _ + 1)

  /**
    * Evolves initial population with genetic operators for a provided time
    * @param settings Set of genetic operators which define iterative cycle
    * @param duration Duration of evolving
    * @return Evolved population
    */
  def iterate[G](settings: AlgoSettings[G], duration: Duration): Population[G] = {
    val start = System.currentTimeMillis()
    iterate[G, Long](settings)(start, _ < start + duration.toMillis, _ => System.currentTimeMillis())
  }

  protected def iterate[G, B](settings: AlgoSettings[G])(start: B, until: B => Boolean, click: B => B): Population[G] = {
    def loop(pop: Population[G], condition: B): Population[G] =
      if (until(condition)) loop(settings.cycle(pop), click(condition))
      else pop

    loop(settings.initialPopulation, start)
  }
}
