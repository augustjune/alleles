package examples.matrix


import cats.kernel.Semigroup
import examples.matrix.matrices.{FlowMatrix, MatrixSource, RangeMatrix}
import genetic._
import genetic.genotype._
import genetic.genotype.syntax._
import genetic.operators.mixing.ClassicCrossover
import genetic.operators.mutation.ComplexMutation
import genetic.operators.selection.Tournament

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

object Run extends App {
  for (i <- 1 to 10) {
    println(i + ".")
  RRandom.setSeed(1615587514)
  val (flow, range): (FlowMatrix, RangeMatrix) = new MatrixSource("http://anjos.mgi.polymtl.ca/qaplib/data.d/had20.dat").toMatrices

  implicit val fitness: Fitness[Permutation] = Permutation.fitness(range, flow)
  implicit val combinator: Semigroup[Permutation] = (perm1: Permutation, perm2: Permutation) => perm1.crossover(perm2)
  implicit val mutator: RandomChange[Permutation] = (perm: Permutation) => perm.mutate
  implicit val source: Scheme[Permutation] = Scheme.pure(() => Permutation.create(flow, range))

  val settings = AlgoSettings[Permutation](100, Tournament(20), ClassicCrossover(0.25), ComplexMutation(0.3, 0.7))

  def evolve(settings: AlgoSettings[Permutation]): Future[Permutation] = Future {
    GeneticAlgorithm.iterate(settings, 200).best
  }

  val evolved: List[Permutation] = Await.result(Future.traverse((1 to 1).toList)(_ => evolve(settings)), Duration.Inf)

  println(s"Best before: ${settings.initialPopulation.best.fitness}")

  evolved.foreach { best =>
    val f = fitness.value(best)
    println(s"Best after: $f")
    if (f < 6950) println(best)
  }

  println(s"Seed was: ${RRandom.seed}")
    println("\n")

  }
  //6922 (OPT)    (8,15,16,14,19,6,7,17,1,12,10,11,5,20,2,3,4,9,18,13)
}
