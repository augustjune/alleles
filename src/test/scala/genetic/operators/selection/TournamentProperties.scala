package genetic.operators.selection

import genetic.{Population, PopulationExtension}
import genetic.genotype.Fitness
import genetic.genotype.syntax._
import genetic.operators.SelectionProperties
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen
import org.scalacheck.Gen._
import org.scalacheck.Prop._

object TournamentProperties extends SelectionProperties("Tournament props") {
  type G = Int
  val implGen: Gen[Tournament] = posNum[Int].map(Tournament(_))
  val populationGen: Gen[Population[(G, Double)]] = for {
    n <- posNum[Int]
    pop <- listOfN(n, arbitrary[Int])
  } yield pop.toVector.withFitnesses

  implicit val fitness: Fitness[G] = identity[G]

  property("Selected individuals are best out of the round") = forAll(populationGen, posNum[Int]) {
    (pop, roundSize) =>
      val tournament = Tournament(roundSize)
      val (g1, g2) = tournament.single(pop)
      val bestOfRound = pop.map(_._2).sorted.takeRight(roundSize).head
      g1.fitness <= bestOfRound && g2.fitness <= bestOfRound
  }

  property("If round transcends population, selected individuals are the same") = forAll(populationGen) { pop =>
    val tournament = Tournament(pop.size + 1)
    val (g1, g2) = tournament.single(pop)
    g1 == g2
  }
}
