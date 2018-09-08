package genetic.examples.matrix

import concurrent.duration._
import genetic.{AlgoSettings, GeneticAlgorithm, Genotype, Population}
import genetic.examples.matrix.matrices.{FlowMatrix, MatrixSource, RangeMatrix}
import genetic.operators.mixing.ClassicCrossover
import genetic.operators.mutation.{ComplexMutation, SingleChromosomeMutation}
import genetic.operators.selection.{Roulette, Tournament}

object Run extends App {
  val (flow, range): (FlowMatrix, RangeMatrix) = new MatrixSource("/home/jura/Projects/jslinkin/allele/src/main/resources/matrix_12.txt").toMatrices
  val permutations = new Permutations(flow, range)
  val init: Population = permutations.candidates.take(100).toList

  val settings = AlgoSettings(Tournament(20), ClassicCrossover(0.5), ComplexMutation(0.2, 0.7))
  val evolved: Population = GeneticAlgorithm.iterate(init, settings, 5 seconds)

  def minFitness(population: Population): Int = population.map(_.fitnessValue).min

  println(s"Best before: ${minFitness(init)}")
  private val after: Int = minFitness(evolved)
  println(s"Best after: ${after}")
  if (after < 1670) println(evolved.minBy(_.fitnessValue))
}
