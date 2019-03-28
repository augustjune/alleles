package genetic.engines

import akka.stream.scaladsl.Source
import genetic.Population
import genetic.engines.async.AsyncSetting
import genetic.engines.bestTracking.BestTrackingSetting
import genetic.genotype.{Fitness, Join, Variation}

import scala.concurrent.ExecutionContext

class Setting(ranking: Ranking, flow: Progress) extends Ambience {
  def evolve[A: Fitness : Join : Variation](options: Epic[A]): EvolutionFlow[Population[A]] =
    Source.repeat(()).scan(options.initialPopulation) {
      case (prev, _) => flow.nextGeneration(ranking.rate(prev), options.operators)
    }

  def bestTracking: BestTrackingSetting = new BestTrackingSetting(ranking, flow)

  def async(implicit executionContext: ExecutionContext): AsyncSetting = new AsyncSetting(flow)
}
