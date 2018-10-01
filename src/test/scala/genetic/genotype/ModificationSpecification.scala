package genetic.genotype

import genetic.RRandom
import genetic.genotype.syntax._
import org.scalacheck
import org.scalacheck.Arbitrary._
import org.scalacheck.Prop._
import org.scalacheck.{Gen, Properties}


/**
  * Any function which allows to make a new instance of type `G`
  * which differs from the original one in random place
  *
  * Laws:
  *   1. Modified instance does not equal to original one
  * modify(g) != g
  *   2. After a certain number of modification the same input produces different outputs
  * def modify5(g: G) = modify(modify(modify(modify(modify(g)))))
  * modify5(g) != modify5(g)
  */
class ModificationSpecification[G: Modification: scalacheck.Gen] extends Properties("Modification laws") {
  val gen = implicitly[Gen[G]]

  property("Modified instance does not equals to original one") = forAll(gen) { g: G =>
    g.mutated != g
  }

  property("After a certain number of modification the same input produces different outputs") =
    forAll(gen) { g: G =>
      def modify5(g: G) = g.mutated.mutated.mutated.mutated.mutated

      modify5(g) != modify5(g)
    }
}

object O extends App {
  val buffer = "The quick brown fox jumps over the lazy dog"
  implicit val modification: Modification[String] =
    (g: String) => g.updated(RRandom.nextInt(g.length), buffer(RRandom.nextInt(buffer.length)))
  val nonEmptyStringGen = Gen.nonEmptyListOf[Char](arbChar.arbitrary).map(_.mkString)
  new ModificationSpecification[String]()(modification, nonEmptyStringGen)
}
