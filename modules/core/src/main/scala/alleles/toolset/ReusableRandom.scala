package alleles.toolset

import scala.collection.BitSet
import scala.collection.generic.CanBuildFrom
import scala.language.higherKinds

/**
  * Implementation of scala.util.Random with available seed lookup for further reuse
  */
class ReusableRandom(private var s: Long) extends util.Random(s) {
  def seed: Long = s

  override def setSeed(seed: Long): Unit = {
    s = seed
    super.setSeed(seed)
  }


  /**
    * Takes `n` elements from original population in random order
    * Replaces combination of `shuffle` and `take` functions,
    * which doesn't perform redundant shuffling
    */
  def take[A, Col[T] <: IndexedSeq[T]](n: Int, from: Col[A])(implicit cbf: CanBuildFrom[Col[A], A, Col[A]]): Col[A] = {
    val originalSize = from.size
    if (n >= originalSize) RRandom.shuffle(from)
    else {
      val popBuilder = cbf()
      popBuilder.sizeHint(n)
      var taken = 0
      var takenIndexes = BitSet()
      while (taken < n) {
        val index = RRandom.nextInt(originalSize)
        if (!takenIndexes.contains(index)) {
          takenIndexes += index
          popBuilder += from(index)
          taken += 1
        }
      }
      popBuilder.result()
    }
  }

  def inRange(range: Double): Double = (nextDouble() - 0.5) * 2 * range

  /**
    * Returns a random boolean with custom probability
    *
    * @param chance probability of `true` value
    */
  def shot(chance: Double): Boolean = nextDouble() < chance


  def chooseOne[A](seq: Seq[A]): A = seq(nextInt(seq.length))

  /**
    * Returns a value from the `pool` chosen by corresponding priority
    *
    * @param pool sequence of tuples representing value and corresponding priority
    */
  def chooseByPriorities[A](pool: Seq[(A, Double)]): A = {
    val sum = pool.map(_._2).sum
    chooseByChances(pool.map { case (x, p) => x -> (p / sum) })
  }

  /**
    * Returns a value from the `pool` chosen by corresponding chance
    * Note: total sum of all chances must equal 1
    */
  def chooseByChances[A](pool: Seq[(A, Double)]): A = choose(nextDouble(), pool.toList)

  private def choose[A](shot: Double, pool: List[(A, Double)]): A = pool match {
    case Nil => throw new RuntimeException("Pool of candidates is empty")
    case (x, _) :: Nil => x
    case (x, chance) :: t => if (shot < chance) x else choose(shot - chance, t)
  }
}


object RRandom extends ReusableRandom(util.Random.nextInt)
