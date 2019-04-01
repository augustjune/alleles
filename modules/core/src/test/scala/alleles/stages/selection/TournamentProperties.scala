package alleles.stages.selection

import alleles.genotype.Fitness
import alleles.genotype.syntax._
import alleles.stages.{Selection, SelectionProperties}
import alleles.{Population, PopulationExtension}
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen
import org.scalacheck.Gen._
import org.scalacheck.Prop._

object TournamentProperties extends SelectionProperties("Tournament props") {
  type Ind = Int
  val implGen: Gen[Selection] = posNum[Int].map(Selection.tournament(_))
  val populationGen: Gen[Population[(Ind, Double)]] = for {
    n <- posNum[Int]
    pop <- listOfN(n, arbitrary[Int])
  } yield pop.toVector.withFitnesses

  implicit val fitness: Fitness[Ind] = identity[Ind]

  property("Selected individuals are best out of the round") = forAll(populationGen, posNum[Int]) {
    (pop, roundSize) =>
      val tournament = Selection.tournament(roundSize)
      val (g1, g2) = tournament.pair(pop)
      val bestOfRound = pop.map(_._2).sorted.takeRight(roundSize).head
      g1.fitness <= bestOfRound && g2.fitness <= bestOfRound
  }

  property("If round transcends population, selected individuals are the same") = forAll(populationGen) { pop =>
    val tournament = Selection.tournament(pop.size + 1)
    val (g1, g2) = tournament.pair(pop)
    g1 == g2
  }
}
