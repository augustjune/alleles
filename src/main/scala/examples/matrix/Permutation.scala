package examples.matrix

import cats.kernel.Semigroup
import genetic.{Fitness, Mutator}
import examples.matrix.matrices.{FlowMatrix, RangeMatrix}

import scala.util.Random

object Permutation {

  def fitness(flowMatrix: FlowMatrix, rangeMatrix: RangeMatrix): Fitness[Permutation] = (perm: Permutation) => {
    val locationMap: Map[Int, Int] = perm.locations.zipWithIndex.toMap

    def relationPrices(currentLocation: Int, currentNum: Int): Int =
      locationMap.foldLeft(0) { case (left, (loc, num)) => left + rangeMatrix(currentLocation - 1, loc - 1) * flowMatrix(currentNum, num) }

    locationMap.foldLeft(0) { case (left, (loc, num)) => left + relationPrices(loc, num) }
  }

  def cacheFitness(flowMatrix: FlowMatrix, rangeMatrix: RangeMatrix): Fitness[Permutation] = cacheFitness(fitness(flowMatrix, rangeMatrix))

  def cacheFitness(inner: Fitness[Permutation]): Fitness[Permutation] = new Fitness[Permutation] {
    private var cache: Map[Permutation, Int] = Map.empty

    def value(perm: Permutation): Int = cache.get(perm) match {
      case Some(value) => value
      case None =>
        val value = inner.value(perm)
        cache += (perm -> value)
        value
    }
  }


  val mutator: Mutator[Permutation] = (perm: Permutation) => {
    def switchPair(n1: Int, n2: Int): Permutation = {
      val val1 = perm.locations(n1)
      new Permutation(perm.locations.updated(n1, perm.locations(n2)).updated(n2, val1))
    }

    switchPair(Random.nextInt(perm.size), Random.nextInt(perm.size))
  }

  val crossover: Semigroup[Permutation] = (x: Permutation, y: Permutation) => x.crossover(y)
}

class Permutation(private val locations: Seq[Int]) {
  val size: Int = locations.length

  def greedyGuess(implicit f: Fitness[Permutation]): Permutation = {
    def pairSwitchedPerm: Stream[Permutation] =
      for {
        i <- locations.indices.toStream
        j <- i + 1 until size
      } yield switchPair(i, j)

    pairSwitchedPerm.find(f.value(_) < f.value(this)) match {
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

    new Permutation(repair(locations.take(num) ++ other.locations.drop(num)))
  }

  def mutate: Permutation = switchPair(Random.nextInt(size), Random.nextInt(size))

  def switchPair(n1: Int, n2: Int): Permutation = {
    val loc1 = locations(n1)
    new Permutation(locations.updated(n1, locations(n2)).updated(n2, loc1))
  }

  override def toString: String = locations mkString("[", ", ", "]")
}
