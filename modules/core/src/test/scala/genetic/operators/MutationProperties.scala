package genetic.operators

import genetic.Population
import genetic.genotype.Variation
import org.scalacheck.Gen._
import org.scalacheck.Prop._
import org.scalacheck.{Gen, Properties}

abstract class MutationProperties(name: String) extends Properties(name + " with Mutation props") {
  type Ind
  implicit def variation: Variation[Ind]
  def gGen: Gen[Ind]

  def implGen: Gen[Mutation]

  def populationGen: Gen[Population[Ind]] = for {
    n <- posNum[Int]
    pop <- listOfN(n, gGen)
  } yield pop.toVector

  property("Generation of mutated individuals holds the same size") = forAll(implGen, populationGen) {
    (implementation, pop) => implementation.generation(pop).size == pop.size
  }
}
