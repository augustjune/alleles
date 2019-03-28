package genetic.engines

import genetic.engines.parallel.ParallelRanking
import genetic.engines.parallel.configurable.ConfigurableParRanking
import genetic.engines.sequential.SeqRanking
import genetic.genotype.syntax.FitnessObj
import genetic.{GenotypeImplicits, Population}
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen._
import org.scalacheck.Prop._
import org.scalacheck.{Gen, Properties}

import scala.collection.parallel.{ForkJoinTaskSupport, TaskSupport}


object FitnessEvaluatorTest extends Properties("Fitness evaluator test") {
  val derivative: Gen[Ranking] = Gen.oneOf(
    SeqRanking,
    ParallelRanking,
    new ConfigurableParRanking(new ForkJoinTaskSupport)
  )

  type Ind = Int
  implicit val fitness = GenotypeImplicits[Ind].fitness
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
      population.groupBy(identity).mapValues(_.size)

    countElems(population1) == countElems(population2)
  }
}
