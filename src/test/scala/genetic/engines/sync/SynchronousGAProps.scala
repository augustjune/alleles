package genetic.engines.sync

import genetic.genotype._
import genetic.operators.crossover.ParentsOrOffspring
import genetic.operators.mutation.RepetitiveMutation
import genetic.operators.{Crossover, Mutation, Selection}
import genetic.operators.selection._
import genetic.{GenotypeImplicits, OperatorSet, Population}
import org.scalacheck.{Gen, Prop, Properties}
import org.scalacheck.Prop._
import org.scalacheck.Gen._

import concurrent.duration._

abstract class SynchronousGAProps(name: String) extends Properties(name + " with SynchronousGA props") {
  def implGen: Gen[SynchronousGA]

  type G

  val implicits: GenotypeImplicits[G]

  import implicits._

  def popGen: Gen[Population[G]] = smallNumberGen.map(Scheme.make(_))

  def operatorsGen: Gen[OperatorSet] = for {
    selection <- selectionGen
    crossover <- crossoverGen
    mutation <- mutationGen
  } yield OperatorSet(selection, crossover, mutation)

  def selectionGen: Gen[Selection] = tournamentGen //oneOf(tournamentGen, rouletteGen)

  def tournamentGen: Gen[Tournament] = sized(n => choose(1, math.max(1, n))).map(Tournament(_))

  def rouletteGen: Gen[Selection] = Gen.const(Roulette)

  def crossoverGen: Gen[Crossover] = choose(0.0, 1.0).map(ParentsOrOffspring(_))

  def mutationGen: Gen[Mutation] = for {
    indChance <- choose(0.0, 1.0)
    repChance <- choose(0.0, 1.0)
  } yield RepetitiveMutation(indChance, repChance)

  def smallNumberGen: Gen[Int] = choose(1, 50)

  def smallDurationGen: Gen[Duration] = choose(10, 1000).map(x => Duration.fromNanos(x * 1000000))


  property("Evolution holds population size") = forAll(implGen, popGen :| "Population", operatorsGen, smallNumberGen :| "IterationsN") {
    (ga, population, operators, iterations) =>
      val originalSize = population.length
      val evolvedSize = ga.evolve(population, operators, iterations).length
      s"Original size: $originalSize, evolvedSize: $evolvedSize" |: Prop.atLeastOne(
        evolvedSize ?= originalSize,
        evolvedSize ?= originalSize - 1
      )
  }

  property("Genetic algorithms creates population of proper size") =
    forAll(implGen, oneOf[Int](10, 20, 30, 40, 50), operatorsGen, smallNumberGen) {
      (ga, popSize, operators, iterations) =>
        val evolved = ga.createAndEvolve(popSize, operators, iterations)
        evolved.length ?= popSize
    }

  property("Evolution provides to better population") = forAll(implGen, popGen, crossoverGen, smallNumberGen :| "IterationsN") {
    (ga, originalPop, crossover, iterations) =>
      def averageFitness(pop: Population[G]) = if (pop.isEmpty) 0.0 else pop.map(Fitness(_)).sum / pop.length

      val ops = OperatorSet(Tournament(originalPop.size + 1 / 2), crossover, RepetitiveMutation(0.0, 0.0))
      val evolved = ga.evolve(originalPop, ops, iterations)
      val evolvedAvg = averageFitness(evolved)
      val originalAvg = averageFitness(originalPop)

      s"Original population: $originalPop \nEvolved population: $evolved\n" +
        s"Original average fitness: $originalAvg; Evolved average fitness: $evolvedAvg" |: (evolvedAvg <= originalAvg)
  }

  val breakingTimeMillis = 100
  property("Evolution ends in certain time") = forAll(implGen, popGen, operatorsGen, smallDurationGen) {
    (ga, originalPop, operators, duration) =>
      within(duration.toMillis + breakingTimeMillis) {
        ga.evolve(originalPop, operators, duration)
        passed
      }
  }

}
