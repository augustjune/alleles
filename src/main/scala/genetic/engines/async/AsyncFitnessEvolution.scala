package genetic.engines.async

import akka.NotUsed
import akka.stream.scaladsl.Source
import genetic.engines.EvolutionStrategy
import genetic.genotype.{Join, Modification}
import genetic.{OperatorSet, Population}

import scala.concurrent.{ExecutionContext, Future}

class AsyncFitnessEvolution(strategy: EvolutionStrategy)(implicit executionContext: ExecutionContext) {
  def evolve[G: AsyncFitness : Join : Modification](initial: Population[G], operators: OperatorSet): Source[Population[G], NotUsed] = {
    Source.repeat(()).scanAsync(initial) {
      case (prev, _) => Future.traverse(prev) { g =>
        for {
          a <- Future.successful(g)
          b <- AsyncFitness(g)
        } yield (a, b)
      }.map(strategy.evolutionStep(_, operators))
    }
  }
}
