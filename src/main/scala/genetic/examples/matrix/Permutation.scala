package genetic.examples.matrix

import genetic.examples.matrix.matrices.{FlowMatrix, RangeMatrix}

import scala.util.Random

class Permutation(flowMatrix: FlowMatrix, rangeMatrix: RangeMatrix, private val locations: Seq[Int]) {
  require(flowMatrix.size == rangeMatrix.size)
  require(flowMatrix.size == locations.length)

  val size: Int = locations.length

  lazy val fitnessValue: Int = {
    val locationMap: Map[Int, Int] = locations.zipWithIndex.toMap

    def relationPrices(currentLocation: Int, currentNum: Int): Int =
      locationMap.foldLeft(0) { case (left, (loc, num)) => left + rangeMatrix(currentLocation - 1, loc - 1) * flowMatrix(currentNum, num) }

    locationMap.foldLeft(0) { case (left, (loc, num)) => left + relationPrices(loc, num) }
  }

  def switchPair(n1: Int, n2: Int): Permutation = {
    val loc1 = locations(n1)
    create(locations.updated(n1, locations(n2)).updated(n2, loc1))
  }

  def greedyGuess: Permutation = {
    def pairSwitchedPerm: Stream[Permutation] =
      for {
        i <- locations.indices.toStream
        j <- i + 1 until size
      } yield switchPair(i, j)

    pairSwitchedPerm.find(_.fitnessValue < fitnessValue) match {
      case Some(betterPerm) => betterPerm
      case None => this
    }
  }

  def crossover(other: Permutation): Permutation = crossover(other.asInstanceOf[Permutation], Random.nextInt(size))

  def crossover(other: Permutation, num: Int): Permutation = {
    def repair(locations: Seq[Int]): Seq[Int] = {
      def replaceDuplicates(pool: List[Int], loc: List[Int]): List[Int] = loc match {
        case h :: t =>
          if (t.contains(h)) pool.head :: replaceDuplicates(pool.tail, t)
          else h :: replaceDuplicates(pool, t)
        case Nil => Nil
      }

      val missing = 1 to size filterNot locations.contains
      replaceDuplicates(missing.toList, locations.toList)
    }

    create(repair(locations.take(num) ++ other.locations.drop(num)))
//    ???
  }

  def mutate: Permutation = switchPair(Random.nextInt(size), Random.nextInt(size))

  private def create(locations: Seq[Int]) = new Permutation(flowMatrix, rangeMatrix, locations)

  override def toString: String = locations mkString("[", ", ", "]")
}
