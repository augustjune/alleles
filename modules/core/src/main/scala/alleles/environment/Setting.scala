package alleles.environment

import akka.stream.scaladsl.Source
import alleles.Population
import alleles.environment.async.AsyncSetting
import alleles.environment.bestTracking.BestTrackingSetting
import alleles.genotype.{Fitness, Join, Variation}

import scala.concurrent.ExecutionContext

class Setting(ranking: Ranking, flow: Progress) extends Ambience {
  def evolve[A: Fitness : Join : Variation](epic: Epic[A]): EvolutionFlow[Population[A]] =
    Source.repeat(()).scan(epic.initialPopulation) {
      case (prev, _) => flow.nextGeneration(ranking.rate(prev), epic.operators)
    }

  def bestTracking: BestTrackingSetting = new BestTrackingSetting(ranking, flow)

  def async(implicit executionContext: ExecutionContext): AsyncSetting = new AsyncSetting(flow)
}
