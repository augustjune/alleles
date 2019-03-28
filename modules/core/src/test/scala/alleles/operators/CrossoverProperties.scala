package alleles.operators

import alleles.Population
import alleles.genotype.Join
import org.scalacheck.{Gen, Properties}
import org.scalacheck.Prop._
import org.scalacheck.Gen._


abstract class CrossoverProperties(name: String) extends Properties(name + " with Crossover props") {
  type Ind
  def implGen: Gen[Crossover]
  def gPairGen: Gen[(Ind, Ind)]

  implicit def join: Join[Ind]

  def popOfPairsGen: Gen[Population[(Ind, Ind)]] = for {
    n <- posNum[Int]
    pop <- listOfN(n, gPairGen)
  } yield pop.toVector

  property("Generation of joined individuals holds the same size") = forAll(popOfPairsGen, implGen) {
    (pop, implementation) => implementation.generation(pop).size == pop.size * 2
  }
}
