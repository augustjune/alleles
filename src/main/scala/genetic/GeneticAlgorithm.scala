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
  def iterate[A](initial: Population[A], settings: AlgoSettings[A], times: Int): Population[A] =
    iterate[A, Int](initial, settings)(0, _ < times, _ + 1)

  /**
    * Evolves initial population with genetic operators for a provided time
    * @param initial Initial population
    * @param settings Set of genetic operators which define iterative cycle
    * @param duration Duration of evolving
    * @return Evolved population
    */
  def iterate[A](initial: Population[A], settings: AlgoSettings[A], duration: Duration): Population[A] = {
    val start = System.currentTimeMillis()
    iterate[A, Long](initial, settings)(start, _ < start + duration.toMillis, _ => System.currentTimeMillis())
  }

  protected def iterate[A, B](initial: Population[A], settings: AlgoSettings[A])(start: B, until: B => Boolean, click: B => B): Population[A] = {
    def loop(pop: Population[A], condition: B): Population[A] =
      if (until(condition)) loop(settings.cycle(pop), click(condition))
      else pop

    loop(initial, start)
  }
}
