package genetic

import scala.concurrent.duration.Duration

object GeneticAlgorithm {
  /**
    * Evolves initial population with genetic operators for a finite number of iterations
    * @param initial Initial population
    * @param settings Set of genetic operators which define iterative cycle
    * @param times Number of iteration
    * @return Evolved population
    */
  def iterate(initial: Population, settings: AlgoSettings, times: Int): Population =
    iterate[Int](initial, settings)(0, _ < times, _ + 1)

  /**
    * Evolves initial population with genetic operators for a provided time
    * @param initial Initial population
    * @param settings Set of genetic operators which define iterative cycle
    * @param duration Duration of evolving
    * @return Evolved population
    */
  def iterate(initial: Population, settings: AlgoSettings, duration: Duration): Population = {
    val start = System.currentTimeMillis()
    iterate[Long](initial, settings)(start, _ < start + duration.toMillis, _ => System.currentTimeMillis())
  }

  protected def iterate[B](initial: Population, settings: AlgoSettings)(start: B, until: B => Boolean, click: B => B): Population = {
    def loop(pop: Population, condition: B): Population =
      if (until(condition)) loop(settings.cycle(pop), click(condition))
      else pop

    loop(initial, start)
  }
}
