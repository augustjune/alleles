package alleles.environment.async

import alleles.Population
import alleles.environment.{Epic, Progress}
import alleles.genotype.{Join, Variation}
import cats.Traverse
import cats.effect.IO
import cats.instances.vector._
import fs2.Stream

import scala.concurrent.ExecutionContext

/**
  * Implementation of genetic algorithm with asynchronous fitness value computation and
  * parametrized of applying genetic operators to populations
  */
class AsyncSetting[A](flow: Progress[A])(implicit executionContext: ExecutionContext,
                                      F: AsyncFitness[IO, A],
                                      J: Join[A],
                                      V: Variation[A]) {

  def evolve(epic: Epic[A]): Stream[IO, Population[A]] = {
    Stream(()).repeat.covary[IO].evalScan(epic.initialPopulation) {
      case (prev, _) => Traverse[Vector].traverse(prev) { g =>
        for {
          individual <- IO.pure(g)
          fitness <- AsyncFitness(g)
        } yield (individual, fitness)
      }.map(flow.nextGeneration(_, epic.operators))
    }
  }
}
