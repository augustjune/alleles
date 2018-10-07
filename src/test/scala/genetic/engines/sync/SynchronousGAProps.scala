package genetic.engines.sync

import genetic.genotype._
import genetic.operators.crossover.ParentsOrOffspring
import genetic.operators.mutation.RepetitiveMutation
import genetic.operators.{Crossover, Mutation, Selection}
import genetic.operators.selection._
import genetic.{GenotypeImplicits, OperatorSet, Population}
import org.scalacheck.{Gen, Properties}
import org.scalacheck.Prop._
import org.scalacheck.Gen._
import org.scalacheck.Arbitrary._

abstract class SynchronousGAProps(name: String) extends Properties(name + " with SynchronousGA props") {
  def implGen: Gen[SynchronousGA]

  type G = String
  val implicits = GenotypeImplicits[G]

  import implicits._

  def popGen: Gen[Population[G]] = for {
    head <- alphaStr
    tail <- nonEmptyListOf(alphaStr)
  } yield head +: tail

  def operatorsGen: Gen[OperatorSet] = for {
    selection <- selectionGen
    crossover <- crossoverGen
    mutation <- mutationGen
  } yield OperatorSet(selection, crossover, mutation)

  def selectionGen: Gen[Selection] = oneOf(tournamentGen, rouletteGen)

  def tournamentGen: Gen[Tournament] = sized(n => choose(1, math.max(1, n))).map(Tournament(_))

  def rouletteGen: Gen[Selection] = Gen.const(Roulette)

  def crossoverGen: Gen[Crossover] = choose(0.0, 1.0).map(ParentsOrOffspring(_))

  def mutationGen: Gen[Mutation] = for {
    indChance <- choose(0.0, 1.0)
    repChance <- choose(0.0, 1.0)
  } yield RepetitiveMutation(0.0, repChance)

  def iterationNumberGen: Gen[Int] = choose(1, 100)

  property("Evolution holds population size") = forAll(implGen, popGen :| "Population", operatorsGen, iterationNumberGen :| "IterationsN") {
    (ga, population, operators, iterations) =>
      val evolvedSize = ga.evolve(population, operators, iterations).length
      val originalSize = population.length
      evolvedSize == originalSize || evolvedSize == originalSize - 1
  }

  property("Average fitness is better or same") = forAll(implGen, popGen :| "Population", operatorsGen, iterationNumberGen :| "IterationsN") {
    (ga, originalPop, operators, iterations) =>
      def averageFitness(pop: Population[G]): Double = pop.map(Fitness(_)).sum / pop.size
      val evolved = ga.evolve(originalPop, operators, iterations)
      averageFitness(evolved) <= averageFitness(originalPop)
  }
}
