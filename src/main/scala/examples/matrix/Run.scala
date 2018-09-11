package examples.matrix


import cats.kernel.Semigroup

import concurrent.duration._
import genetic.{AlgoSettings, GeneticAlgorithm, Population}
import examples.matrix.matrices.{FlowMatrix, MatrixSource, RangeMatrix}
import genetic.genotype._
import genetic.operators.mixing.ClassicCrossover
import genetic.operators.mutation.ComplexMutation
import genetic.operators.selection.{Roulette, Tournament}

import scala.language.postfixOps

object Run extends App {
  val (flow, range): (FlowMatrix, RangeMatrix) = new MatrixSource("/home/jura/Projects/jslinkin/allele/src/main/resources/matrix_16.txt").toMatrices

  implicit val fitness: Fitness[Permutation] = Permutation.fitness(range, flow)
  implicit val combinator: Semigroup[Permutation] = (perm1: Permutation, perm2: Permutation) => perm1.crossover(perm2)
  implicit val mutator: RandomChange[Permutation] = (perm: Permutation) => perm.mutate
  implicit val source: Design[Permutation] = Design.pure(() => Permutation.create(flow, range))

  val settings = AlgoSettings[Permutation](100, Tournament(20), ClassicCrossover(0.5), ComplexMutation(0.2, 0.7))
  val evolved: Population[Permutation] = GeneticAlgorithm.iterate(settings, 3 seconds)

  def minFitness(population: Population[Permutation]): Int = population.map(fitness.value).min

  println(s"Best before: ${minFitness(settings.initialPopulation)}")
  private val after: Int = minFitness(evolved)
  println(s"Best after: $after")
  if (after < 1670) println(evolved.minBy(fitness.value))
}
