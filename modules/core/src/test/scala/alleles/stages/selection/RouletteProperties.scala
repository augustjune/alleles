package alleles.stages.selection

import alleles.stages.{Selection, SelectionProperties}
import org.scalacheck.Gen


object RouletteProperties extends SelectionProperties("Roulette props") {
  val implGen: Gen[Selection] = Gen.const(Selection.roulette)
}
