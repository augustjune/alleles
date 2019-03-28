package genetic.engines.async

import akka.stream.scaladsl.Source
import genetic.engines.{Progress, EvolutionFlow}
import genetic.genotype.{Join, Variation}
import genetic.{Epoch, Population}

import scala.concurrent.{ExecutionContext, Future}

class AsyncSetting(flow: Progress)(implicit executionContext: ExecutionContext) {
  def evolve[A: AsyncFitness : Join : Variation](initial: Population[A],
                                                 operators: Epoch): EvolutionFlow[Population[A]] = {
    Source.repeat(()).scanAsync(initial) {
      case (prev, _) => Future.traverse(prev) { g =>
        for {
          a <- Future.successful(g)
          b <- AsyncFitness(g)
        } yield (a, b)
      }.map(flow.nextGeneration(_, operators))
    }
  }
}
