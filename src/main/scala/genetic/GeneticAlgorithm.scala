package genetic

import scala.concurrent.duration.Duration

object GeneticAlgorithm {
  def iterate[B](initial: Population, settings: AlgoSettings)(start: B, until: B => Boolean, click: B => B): Population = {
    def loop(pop: Population, condition: B): Population =
      if (until(condition)) loop(settings.cycle(pop), click(condition))
      else pop

    loop(initial, start)
  }

  def iterate(initial: Population, settings: AlgoSettings, times: Int): Population =
    iterate[Int](initial, settings)(0, _ < times, _ + 1)

  def iterate(initial: Population, settings: AlgoSettings, duration: Duration): Population = {
    val start = System.currentTimeMillis()
    iterate[Long](initial, settings)(start, _ < start + duration.toMillis, _ => System.currentTimeMillis())
  }
}
