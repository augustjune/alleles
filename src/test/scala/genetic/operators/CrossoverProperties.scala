package genetic.operators

import genetic.Population
import genetic.genotype.Join
import org.scalacheck.{Gen, Properties}
import org.scalacheck.Prop._
import org.scalacheck.Gen._


abstract class CrossoverProperties(name: String) extends Properties(name + " with Crossover props") {
  type G
  def implGen: Gen[Crossover]
  def gPairGen: Gen[(G, G)]

  implicit def join: Join[G]

  def popOfPairsGen: Gen[Population[(G, G)]] = for {
    n <- posNum[Int]
    pop <- listOfN(n, gPairGen)
  } yield pop.toVector

  property("Generation of joined individuals holds the same size") = forAll(popOfPairsGen, implGen) {
    (pop, implementation) => implementation.generation(pop).size == pop.size * 2
  }
}
