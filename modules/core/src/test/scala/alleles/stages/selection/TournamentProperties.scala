package alleles.stages.selection

import alleles.genotype.Fitness
import alleles.genotype.syntax._
import alleles.stages.{Selection, SelectionProperties}
import org.scalacheck.Gen
import org.scalacheck.Gen._
import org.scalacheck.Prop._

object TournamentProperties extends SelectionProperties("Tournament props") {
  val implGen: Gen[Selection] = posNum[Int].map(Selection.tournament(_))

  implicit val fitness: Fitness[Ind] = _.length.toDouble

  property("Selected individuals are best out of the round") =
    forAll(populationGen, posNum[Int]) { (pop, roundSize) =>
      val tournament = Selection.tournament(roundSize)
      val (g1, g2) = tournament.pair(pop)
      val bestOfRound = pop.map(_._2).sorted.takeRight(roundSize).head
      g1.fitness <= bestOfRound && g2.fitness <= bestOfRound
    }
}
