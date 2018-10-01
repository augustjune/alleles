package genetic.operators.crossover

import genetic.collections.IterablePair
import genetic.genotype.Join
import genetic.genotype.syntax._
import org.scalacheck.Arbitrary._
import org.scalacheck.Prop._
import org.scalacheck.{Arbitrary, Cogen, Gen, Properties}

abstract class ParentsOrOffspringSpecification[G: Arbitrary: Cogen](GName: String) extends Properties(s"Parents or offspring of type $GName") {

  val parentsOrOffspringGen: Gen[ParentsOrOffspring] = arbitrary[Double].map(ParentsOrOffspring(_))
  val joinGen: Gen[Join[G]] = arbitrary[(G, G) => G].map(f => (x: G, y: G) => f(x, y))

  property("Either parents or offspring") = forAll(parentsOrOffspringGen, joinGen, arbitrary[G], arbitrary[G]) {
    (p: ParentsOrOffspring, join: Join[G], g1: G, g2: G) =>
      implicit val i = join
      val parents = IterablePair(g1, g2)
      val offspring = g1 >< g2
      val result = p.single(g1, g2)
      result == parents || result == offspring
  }

  property("100% parents") = forAll(joinGen, arbitrary[G], arbitrary[G]) { (join: Join[G], p1: G, p2: G) =>
    implicit val i = join
    ParentsOrOffspring(1).single(p1, p2) == IterablePair(p1, p2)
  }

  property("0% parents") = forAll(joinGen, arbitrary[G], arbitrary[G]) { (join: Join[G], p1: G, p2: G) =>
    implicit val i = join
    ParentsOrOffspring(0).single(p1, p2) == p1 >< p2
  }
}

object StringSpec extends ParentsOrOffspringSpecification[String]("String")
object IntSpec extends ParentsOrOffspringSpecification[Int]("Int")
