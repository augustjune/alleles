package genetic.engines.async

import akka.NotUsed
import akka.stream.scaladsl.Source
import genetic.engines.Evolution
import genetic.genotype.{Join, Modification}
import genetic.{OperatorSet, Population}

import scala.concurrent.{ExecutionContext, Future}

class AsyncFitnessDriver(flow: Evolution)(implicit executionContext: ExecutionContext) {
  def evolve[G: AsyncFitness : Join : Modification](initial: Population[G], operators: OperatorSet): Source[Population[G], NotUsed] = {
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
