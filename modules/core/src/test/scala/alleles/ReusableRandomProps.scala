package alleles

import alleles.toolset.RRandom
import org.scalacheck.Properties
import org.scalacheck.Prop._
import org.scalacheck.Gen._
import org.scalacheck.Arbitrary._

object ReusableRandomProps extends Properties("Reusable Random properties") {

  implicit class VectorOps[T](v: Vector[T]) {
    def includes(other: Vector[T]): Boolean = {
      val thisMap = v.groupBy(identity).mapValues(_.size)
      val otherMap = other.groupBy(identity).mapValues(_.size)
      otherMap.forall {
        case (t, n) => thisMap.get(t) match {
          case None => false
          case Some(x) => n <= x
        }
      }
    }
  }

  property("Random chooses from seq") = forAll(nonEmptyListOf(arbInt)) { l =>
    val chosen = RRandom.chooseOne(l)
    l.contains(chosen)
  }

  property("Random takes subset of collection") = forAll { (l1: Vector[Int], l2: Vector[Int]) =>
    val whole = l1 ++ l2
    val takeSize = l1.size
    val shuffled = RRandom.take(takeSize, whole)
    shuffled.size == takeSize && whole.includes(shuffled)
  }

  property("Same numbers with the same seed") = forAll(choose(0, 1000)) { n: Int =>
    val seed = RRandom.nextLong()
    RRandom.setSeed(seed)
    val l1 = (1 to n).toList.map(_ => RRandom.nextInt())
    RRandom.setSeed(seed)
    val l2 = (1 to n).toList.map(_ => RRandom.nextInt())
    l1 == l2
  }
}
