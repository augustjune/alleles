package genetic.examples.matrix

import genetic.Genotype
import genetic.examples.matrix.matrices.{FlowMatrix, RangeMatrix}

import scala.util.Random


class Permutations(flowMatrix: FlowMatrix, rangeMatrix: RangeMatrix) {
  require(flowMatrix.size == rangeMatrix.size)

  def create(locations: Seq[Int]): Genotype = new Permutation(flowMatrix, rangeMatrix, locations)

  def random: Genotype = create(Random.shuffle((1 to flowMatrix.size).toVector))

  def candidates: Stream[Genotype] = random #:: candidates
}
