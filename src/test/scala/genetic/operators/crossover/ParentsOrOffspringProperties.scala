package genetic.operators.crossover

import genetic.collections.IterablePair
import genetic.genotype.Join
import genetic.genotype.syntax._
import genetic.operators.CrossoverProperties
import org.scalacheck.Arbitrary._
import org.scalacheck.Prop._
import org.scalacheck.Gen._
import org.scalacheck._


object ParentsOrOffspringProperties extends CrossoverProperties("ParentsOrOffspring props") {
  type G = String
  val implGen: Gen[ParentsOrOffspring] = choose[Double](0.0, 1.0).map(ParentsOrOffspring(_))
  val gPairGen: Gen[(G, G)] = arbTuple2[String, String].arbitrary

  implicit val join: Join[G] = (x: String, y: String) => x.take(x.length / 2) + y.drop(y.length / 2)

  property("Either parents or offspring") = forAll(gPairGen, implGen) { case ((p1, p2), crossover) =>
    val parents = IterablePair(p1, p2)
    val offspring = p1 >< p2
    val result = crossover.single(p1, p2)
    result == parents || result == offspring
  }

  /**
    * Generator with parents chance 1.00 or more
    */
  val parentsBiasedGen: Gen[ParentsOrOffspring] = sized(n => choose(1.0, math.max(n, 1.0))).map(ParentsOrOffspring)

  property("100% parents") = forAll(gPairGen, parentsBiasedGen) {
    case ((p1, p2), crossover) =>
      val result = crossover.single(p1, p2)
      result ?= IterablePair(p1, p2)
  }

  /**
    * Generator with parents chance 0.00 or less
    */
  val offspringBiasedGen: Gen[ParentsOrOffspring] = sized(n => choose(-n, 0.0)).map(ParentsOrOffspring)

  property("0% parents") = forAll(gPairGen, offspringBiasedGen) {
    case ((p1, p2), crossover) =>
      val result = crossover.single(p1, p2)
      val offspring = p1 >< p2
      result ?= offspring
  }
}
