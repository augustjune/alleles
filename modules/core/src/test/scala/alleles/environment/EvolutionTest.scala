package alleles.environment

import alleles.environment.parallel.ParallelProgress
import alleles.environment.parallel.configurable.ConfigurableParProgress
import alleles.environment.sequential.SeqProgress
import alleles.genotype.Fitness
import alleles.stages._
import alleles.{Epoch, GenotypeImplicits, Population}
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen._
import org.scalacheck.Prop._
import org.scalacheck.{Gen, Properties}

import scala.collection.parallel.ForkJoinTaskSupport

object EvolutionTest extends Properties("Evolution strategy props") {
  type Ind = Int
  val implicits: GenotypeImplicits[Ind] = GenotypeImplicits[Ind]

  import implicits._

  val derivative: Gen[Progress[Ind]] = Gen.oneOf(
    new SeqProgress,
    new ParallelProgress,
    new ConfigurableParProgress(new ForkJoinTaskSupport)
  )

  val populationGen: Gen[Population[Ind]] = nonEmptyListOf(arbInt.arbitrary).map(_.toVector)

  val selectionGen: Gen[Selection] = Gen.const(Selection.tournament(10))
  val crossoverGen: Gen[CrossoverStrategy[Ind]] = Gen.const(CrossoverStrategy.parentsOrOffspring(0.25))
  val mutationGen: Gen[MutationStrategy[Ind]] = Gen.const(MutationStrategy.repetitiveMutation(0.5, 0.5))

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
