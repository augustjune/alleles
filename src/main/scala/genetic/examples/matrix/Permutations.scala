package genetic.examples.matrix

import genetic.examples.matrix.matrices.{FlowMatrix, RangeMatrix}

import scala.util.Random


class Permutations(flowMatrix: FlowMatrix, rangeMatrix: RangeMatrix) {
  require(flowMatrix.size == rangeMatrix.size)

  def random: Permutation = new Permutation(Random.shuffle((1 to flowMatrix.size).toVector))

  def candidates: Stream[Permutation] = random #:: candidates
}
