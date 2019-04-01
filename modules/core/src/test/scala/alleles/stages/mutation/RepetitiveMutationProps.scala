package alleles.stages.mutation

import alleles.genotype.Variation
import alleles.stages.{CrossoverStrategy, MutationProperties, MutationStrategy}
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen
import org.scalacheck.Gen._
import org.scalacheck.Prop._


object RepetitiveMutationProps extends MutationProperties("RepetitiveMutation props") {
  type Ind = Int

  def gGen: Gen[Int] = arbitrary[Int]

  def repetitiveMutationGenTemplate(iChanceGen: Gen[Double], repChanceGen: Gen[Double]): Gen[MutationStrategy[Ind]] =
    for (iChance <- iChanceGen; repChance <- repChanceGen) yield MutationStrategy.repetitiveMutation(iChance, repChance)

  def implGen: Gen[MutationStrategy[Ind]] =
    repetitiveMutationGenTemplate(choose(0.0, 1.0), choose(0.0, 1.0))

  implicit def variation: Variation[Int] = _ + 1

  def mutationsOccurred(before: Ind, after: Ind): Int = after - before

  def notApplicableGen: Gen[MutationStrategy[Ind]] =
    repetitiveMutationGenTemplate(sized(n => choose(-n, 0.0)), choose(0.0, 1.0))

  property("If individual chance is 0% individual stays the same") = forAll(gGen :| "G", notApplicableGen) {
    (original, mutation) => mutation.single(original) ?= original
  }

  def alwaysApplicableGen: Gen[MutationStrategy[Ind]] =
    repetitiveMutationGenTemplate(sized(n => choose(1.0, math.max(n, 1.0))), choose(0.0, 1.0))

  property("If individual chance is 100% individual gets mutated at least once") = forAll(gGen :| "G", alwaysApplicableGen) {
    (g, mutation) => mutationsOccurred(g, mutation.single(g)) >= 1
  }

  property("Repetitive chance must be less than 100%") = forAll(arbitrary[Double], sized(n => choose(1.0, math.max(1.0, n)))) {
    (indChance, repChance) => throws(classOf[IllegalArgumentException]) (MutationStrategy.repetitiveMutation(indChance, repChance))
  }

}
