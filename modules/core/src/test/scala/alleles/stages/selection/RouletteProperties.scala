package alleles.stages.selection

import alleles.Population
import alleles.stages.{Selection, SelectionProperties}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalacheck.Gen.{listOfN, posNum}


object RouletteProperties extends SelectionProperties("Roulette props") {
  type Ind = String

  val implGen: Gen[Selection] = Gen.const(Selection.roulette)

  val populationGen: Gen[Population[(Ind, Double)]] = for {
    n <- posNum[Int]
    pop <- listOfN(n, arbitrary[Int])
  } yield pop.map(x => (x.toString, x.toDouble)).toVector
}
