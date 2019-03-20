package genetic

import genetic.genotype._
import org.scalacheck.Arbitrary.arbitrary

import scala.util.Random

trait GenotypeImplicits[Ind] {
  implicit val fitness: Fitness[Ind]
  implicit val join: Join[Ind]
  implicit val variation: Variation[Ind]
  implicit val scheme: Scheme[Ind]
}

object GenotypeImplicits {
  /**
    * Summoner method
    */
  def apply[Ind](implicit implicits: GenotypeImplicits[Ind]): GenotypeImplicits[Ind] = implicits

  /**
    * Implicits set for type Int
    */
  implicit val intImplicits: GenotypeImplicits[Int] = new GenotypeImplicits[Int] {
    val fitness: Fitness[Int] = x => math.abs(x.toDouble)
    val join: Join[Int] = Join.commutative { case (x, y) => (x + y) / 2 }
    val variation: Variation[Int] = _ + 1
    val scheme: Scheme[Int] = () => arbitrary[Int].sample.get
  }

  /**
    * Implicits set for type String
    */
  implicit val stringImplicits: GenotypeImplicits[String] = new GenotypeImplicits[String] {
    val fitness: Fitness[String] = _.length
    val join: Join[String] = Join.singlePoint[String](x => x.splitAt(x.length / 2)) { case (x, y) => x + y }

    private val buffer = "The quick brown fox jumps over the lazy dog".replaceAll(" ", "")
    val variation: Variation[String] = { x =>
      val randomChar = buffer(Random.nextInt(buffer.length))
      if (x.isEmpty) randomChar.toString
      else x.updated(Random.nextInt(x.length), randomChar)
    }

    val scheme: Scheme[String] = () => arbitrary[String].sample.get
  }
}
