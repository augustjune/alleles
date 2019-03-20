package genetic.engines.async

import akka.stream.scaladsl.Source
import genetic.engines.{Evolution, EvolutionFlow}
import genetic.genotype.{Join, Variation}
import genetic.{OperatorSet, Population}

import scala.concurrent.{ExecutionContext, Future}

class AsyncFitnessDriver(flow: Evolution)(implicit executionContext: ExecutionContext) {
  def evolve[A: AsyncFitness : Join : Variation](initial: Population[A],
                                                 operators: OperatorSet): EvolutionFlow[Population[A]] = {
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
