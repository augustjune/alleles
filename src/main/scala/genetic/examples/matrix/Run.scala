package genetic.examples.matrix


import cats.kernel.Semigroup

import concurrent.duration._
import genetic.{AlgoSettings, Fitness, GeneticAlgorithm, Mutator, Population}
import genetic.examples.matrix.matrices.{FlowMatrix, MatrixSource, RangeMatrix}
import genetic.operators.mixing.ClassicCrossover
import genetic.operators.mutation.{ComplexMutation}
import genetic.operators.selection.{Roulette, Tournament}

import scala.language.postfixOps

object Run extends App {
  val (flow, range): (FlowMatrix, RangeMatrix) = new MatrixSource("/home/jura/Projects/jslinkin/allele/src/main/resources/matrix_16.txt").toMatrices
  val permutations = new Permutations(flow, range)

  implicit val fitness: Fitness[Permutation] = (perm: Permutation) => perm.fitnessValue
  implicit val combinator: Semigroup[Permutation] = (perm1: Permutation, perm2: Permutation) => perm1.crossover(perm2)
  implicit val mutator: Mutator[Permutation] = (perm: Permutation) => perm.mutate


  val init: Population[Permutation] = permutations.candidates.take(100).toList

  val settings = AlgoSettings[Permutation](Tournament(20), ClassicCrossover(0.5), ComplexMutation(0.2, 0.7))
  val evolved: Population[Permutation] = GeneticAlgorithm.iterate(init, settings, 3 seconds)

  def minFitness(population: Population[Permutation]): Int = population.map(_.fitnessValue).min

  println(s"Best before: ${minFitness(init)}")
  private val after: Int = minFitness(evolved)
  println(s"Best after: ${after}")
  if (after < 1670) println(evolved.minBy(_.fitnessValue))
}
