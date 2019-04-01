package alleles.environment.async

import akka.stream.scaladsl.Source
import alleles.environment.{EvolutionFlow, Progress}
import alleles.genotype.{Join, Variation}
import alleles.{Epoch, Population}

import scala.concurrent.{ExecutionContext, Future}
// ToDo - add documentation
class AsyncSetting(flow: Progress)(implicit executionContext: ExecutionContext) {
  def evolve[A: AsyncFitness : Join : Variation](initial: Population[A],
                                                 operators: Epoch[A]): EvolutionFlow[Population[A]] = {
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
