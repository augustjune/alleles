package alleles.environment

import alleles.environment.parallel.ParallelProgress
import alleles.environment.parallel.configurable.ConfigurableParProgress
import alleles.environment.sequential.SeqProgress
import alleles.genotype.Fitness
import alleles.operators._
import alleles.operators.crossover.ParentsOrOffspring
import alleles.operators.mutation.RepetitiveMutation
import alleles.operators.selection.Tournament
import alleles.{GenotypeImplicits, Epoch, Population}
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen._
import org.scalacheck.Prop._
import org.scalacheck.{Gen, Properties}

import scala.collection.parallel.{ForkJoinTaskSupport, TaskSupport}

object EvolutionTest extends Properties("Evolution strategy props") {
  val derivative: Gen[Progress] = Gen.oneOf(
    SeqProgress,
    ParallelProgress,
    new ConfigurableParProgress(new ForkJoinTaskSupport)
  )

  type Ind = Int

  val implicits = GenotypeImplicits[Ind]
  import implicits._

  val populationGen: Gen[Population[Ind]] = nonEmptyListOf(arbInt.arbitrary).map(_.toVector)

  val selectionGen: Gen[Selection] = Gen.const(Tournament(10))
  val crossoverGen: Gen[Crossover[Ind]] = Gen.const(new ParentsOrOffspring(0.25))
  val mutationGen: Gen[Mutation] = Gen.const(new RepetitiveMutation(0.5, 0.5))

  val operatorsGen: Gen[Epoch[Ind]] = for {
    sel <- selectionGen
    cross <- crossoverGen
    mut <- mutationGen
  } yield Epoch(sel, cross, mut)

  property("Evolution step shrinks population size to the closest even number") =
    forAll(derivative, populationGen, operatorsGen) {
      (strategy, population, operators) =>
        val originalSize = population.size
        val newSize = strategy.nextGeneration(population.map(x => x -> Fitness(x)), operators).size
        if (originalSize % 2 == 0) newSize == originalSize
        else newSize == originalSize || newSize == originalSize - 1
    }
}
