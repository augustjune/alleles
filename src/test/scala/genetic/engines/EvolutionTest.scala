package genetic.engines

import genetic.engines.parallel.ParallelEvolution
import genetic.engines.parallel.configurable.ConfigurableParEvolution
import genetic.engines.sequential.SeqEvolution$
import genetic.genotype.Fitness
import genetic.operators._
import genetic.operators.crossover.ParentsOrOffspring
import genetic.operators.mutation.RepetitiveMutation
import genetic.operators.selection.Tournament
import genetic.{GenotypeImplicits, OperatorSet, Population}
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen._
import org.scalacheck.Prop._
import org.scalacheck.{Gen, Properties}

import scala.collection.parallel.{ForkJoinTaskSupport, TaskSupport}

object EvolutiongTest extends Properties("Evolution strategy props") {
  val derivative: Gen[Evolution] = Gen.oneOf(
    SeqEvolution$,
    ParallelEvolution,
    new ConfigurableParEvolution(new ForkJoinTaskSupport)
  )

  type Ind = Int

  val populationGen: Gen[Population[Ind]] = nonEmptyListOf(arbInt.arbitrary).map(_.toVector)

  val selectionGen: Gen[Selection] = Gen.const(Tournament(10))
  val crossoverGen: Gen[Crossover] = Gen.const(ParentsOrOffspring(0.25))
  val mutationGen: Gen[Mutation] = Gen.const(RepetitiveMutation(0.5, 0.5))

  val operatorsGen: Gen[OperatorSet] = for {
    sel <- selectionGen
    cross <- crossoverGen
    mut <- mutationGen
  } yield OperatorSet(sel, cross, mut)

  val implicits = GenotypeImplicits[Ind]
  import implicits._

  property("Evolution step shrinks population size to the closest even number") =
    forAll(derivative, populationGen, operatorsGen) {
      (strategy, population, operators) =>
        val originalSize = population.size
        val newSize = strategy.nextGeneration(population.map(x => x -> Fitness(x)), operators).size
        if (originalSize % 2 == 0) newSize == originalSize
        else newSize == originalSize || newSize == originalSize - 1
    }
}
