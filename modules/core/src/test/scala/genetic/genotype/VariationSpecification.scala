package genetic.genotype

import genetic.genotype.syntax._
import genetic.toolset.RRandom
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
object VariationSpecification extends Properties("Variation laws") {

  def specifyFor[Ind](variation: Variation[Ind], gen: Gen[Ind]) = {
    implicit val v = variation

    property("Modified instance does not equals to original one") = forAll(gen) { g: Ind =>
      g.modified != g
    }

    property("After a certain number of modification the same input produces different outputs") =
      forAll(gen) { i: Ind =>
        def modify5(g: Ind) = g.modified.modified.modified.modified.modified

        modify5(i) != modify5(i)
      }

  }

  object StringVariation {
    private val buffer = "The quick brown fox jumps over the lazy dog"
    val gen = Gen.nonEmptyListOf[Char](arbChar.arbitrary).map(_.mkString)
    val variation: Variation[String] =
      (g: String) => g.updated(RRandom.nextInt(g.length), buffer(RRandom.nextInt(buffer.length)))
  }

  specifyFor(StringVariation.variation, StringVariation.gen)

}
