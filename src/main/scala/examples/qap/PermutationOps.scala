package examples.qap

import examples.qap.source._
import genetic.genotype.{Fitness, Join, Modification, Scheme}
import genetic.toolset.RRandom

class PermutationOps(matrixSource: String) {
  val (flow, range): (FlowMatrix, RangeMatrix) = new MatrixSource(matrixSource).toMatrices

  /**
    * Sum of the distances multiplied by the corresponding flows
    */
  implicit val fitness: Fitness[Permutation] = (perm: Permutation) => {
    val locationMap: Map[Int, Int] = perm.zipWithIndex.toMap

    def relationPrices(currentLocation: Int, currentNum: Int): Int =
      locationMap.foldLeft(0) { case (left, (loc, num)) => left + range(currentLocation, loc) * flow(currentNum, num) }

    spin(5)
    locationMap.foldLeft(0) { case (left, (loc, num)) => left + relationPrices(loc, num) }
  }

  def spin(mils: Int): Unit = {
    val start = System.currentTimeMillis()
    while (System.currentTimeMillis() - start < mils) {}
  }

  /**
    * Single-point crossover with fixing violating permutations
    */

  implicit val combinator: Join[Permutation] =
    Join.samePoint[Permutation, Int]((p1, _) => RRandom.nextInt(p1.length), p => p.splitAt(_)) { case (x, y) =>
      def fix(locations: Vector[Int]): Vector[Int] = {
        def replaceDuplicates(loc: List[Int], pool: List[Int]): List[Int] = loc match {
          case h :: t =>
            if (t.contains(h)) pool.head :: replaceDuplicates(t, pool.tail)
            else h :: replaceDuplicates(t, pool)
          case Nil => Nil
        }

        val missing = locations.indices filterNot locations.contains
        replaceDuplicates(locations.toList, missing.toList).toVector
      }

      fix(x ++ y)
    }

  /**
    * Switching two facilities' positions
    */
  implicit val mutator: Modification[Permutation] = (perm: Permutation) => {
    def switchPair(n1: Int, n2: Int): Permutation = {
      val val1 = perm(n1)
      perm.updated(n1, perm(n2)).updated(n2, val1)
    }

    switchPair(RRandom.nextInt(perm.size), RRandom.nextInt(perm.size))
  }

  /**
    * Randomly created permutation, which fits to the flow and range matrices
    */
  implicit val scheme: Scheme[Permutation] = () => RRandom.shuffle((0 until flow.size).toVector)
}
