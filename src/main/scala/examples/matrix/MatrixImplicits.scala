package examples.matrix

import cats.Semigroup
import examples.matrix.matrices._
import genetic.genotype.{Fitness, Mutation, Scheme}

class MatrixImplicits(source: String) {
  val (flow, range): (FlowMatrix, RangeMatrix) = new MatrixSource(source).toMatrices

  implicit val fitness: Fitness[Permutation] = Permutation.fitness(range, flow)
  implicit val combinator: Semigroup[Permutation] = (perm1: Permutation, perm2: Permutation) => perm1.crossover(perm2)
  implicit val mutator: Mutation[Permutation] = (perm: Permutation) => perm.mutate
  implicit val scheme: Scheme[Permutation] = Scheme.pure(() => Permutation.create(flow, range))
}
