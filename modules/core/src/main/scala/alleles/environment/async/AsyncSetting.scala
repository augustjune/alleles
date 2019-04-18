package alleles.environment.async

import akka.stream.scaladsl.Source
import alleles.Population
import alleles.environment.{Ambience, Epic, EvolutionFlow, Progress}
import alleles.genotype.{Join, Variation}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Implementation of genetic algorithm with asynchronous fitness value computation and
  * parametrized of applying genetic operators to populations
  */
class AsyncSetting[A](flow: Progress[A])(implicit executionContext: ExecutionContext,
                                      F: AsyncFitness[A],
                                      J: Join[A],
                                      V: Variation[A]) extends Ambience[A] {

  def evolve(epic: Epic[A]): EvolutionFlow[Population[A]] = {
    Source.repeat(()).scanAsync(epic.initialPopulation) {
      case (prev, _) => Future.traverse(prev) { g =>
        for {
          individual <- Future.successful(g)
          fitness <- AsyncFitness(g)
        } yield (individual, fitness)
      }.map(flow.nextGeneration(_, epic.operators))
    }
  }
}
