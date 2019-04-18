package alleles.environment

import akka.stream.scaladsl.Source
import alleles.Population
import alleles.environment.async.{AsyncFitness, AsyncSetting}
import alleles.environment.bestTracking.BestTrackingSetting
import alleles.genotype.{Fitness, Join, Variation}

import scala.concurrent.ExecutionContext

/**
  * Implementation of evolution environment, which represents genetic algorithm,
  * with parametrized way of rating the population and applying genetic operators to it
  */
class Setting[A: Fitness : Join : Variation](ranking: Ranking[A], flow: Progress[A]) extends Ambience[A] {
  def evolve(epic: Epic[A]): EvolutionFlow[Population[A]] =
    Source.repeat(()).scan(epic.initialPopulation) {
      case (prev, _) => flow.nextGeneration(ranking.rate(prev), epic.operators)
    }

  /**
    * Decorates itself with the ability to track the best individual
    * among the whole evolution process
    */
  def bestTracking: BestTrackingSetting[A] = new BestTrackingSetting[A](ranking, flow)

  /**
    * Decorates itself with the ability to calculate fitness value
    * of individuals asynchronously
    */
  def async(implicit asyncFitness: AsyncFitness[A], executionContext: ExecutionContext): Ambience[A] =
    new AsyncSetting(flow)
}
