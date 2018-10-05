package genetic.operators

import genetic.Population
import genetic.genotype.Modification
import org.scalacheck.{Gen, Properties}
import org.scalacheck.Prop._
import org.scalacheck.Gen._

abstract class MutationProperties(name: String) extends Properties(name + " with Mutation props") {
  type G
  implicit def modification: Modification[G]
  def gGen: Gen[G]

  def implGen: Gen[Mutation]

  def populationGen: Gen[Population[G]] = for {
    n <- posNum[Int]
    pop <- listOfN(n, gGen)
  } yield pop

  property("Generation of mutated individuals holds the same size") = forAll(implGen, populationGen) {
    (implementation, pop) => implementation.generation(pop).size == pop.size
  }
}
