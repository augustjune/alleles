package alleles.environment

import alleles.environment.parallel.ParallelRanking
import alleles.environment.parallel.configurable.ConfigurableParRanking
import alleles.environment.sequential.SeqRanking
import alleles.genotype.Fitness
import alleles.genotype.syntax.FitnessObj
import alleles.{GenotypeImplicits, Population}
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen._
import org.scalacheck.Prop._
import org.scalacheck.{Gen, Properties}

import scala.collection.parallel.ForkJoinTaskSupport


object FitnessEvaluatorTest extends Properties("Fitness evaluator test") {
  type Ind = Int

  implicit val fitness: Fitness[Ind] = GenotypeImplicits[Ind].fitness
  val derivative: Gen[FitnessRanking[Ind]] = Gen.oneOf(
    new SeqRanking,
    new ParallelRanking,
    new ConfigurableParRanking(new ForkJoinTaskSupport)
  )
  val populationGen: Gen[Population[Ind]] = nonEmptyListOf(arbitrary[Ind]).map(_.toVector)

  property("Rating population keeps all individuals") = forAll(derivative, populationGen) {
    (evaluator, population) =>
      containsSame(evaluator.rate(population), population.map(g => g -> g.fitness))
  }

  /*
    Checks if two populations contain same count of each element, despite their order
   */
  def containsSame[A](population1: Population[A], population2: Population[A]): Boolean = {
    def countElems(population: Population[A]): Map[A, Int] =
      population.groupBy(identity).view.mapValues(_.size).toMap

    countElems(population1) == countElems(population2)
  }
}
