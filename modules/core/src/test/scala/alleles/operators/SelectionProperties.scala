package alleles.operators

import alleles.Population
import org.scalacheck.Prop._
import org.scalacheck.{Gen, Prop, Properties}


abstract class SelectionProperties(name: String) extends Properties(name + " with Selection props") {
  type Ind

  def populationGen: Gen[Population[(Ind, Double)]]

  def implGen: Gen[Selection]

  property("Individuals are selected from the population") = forAll(implGen, populationGen) {
    (implementation, pop) =>
      val (i1, i2) = implementation.single(pop)
      val individuals  = pop.map(_._1)
      individuals.contains(i1) && individuals.contains(i2)
  }

  property("Generation of selected individuals holds the same size") = forAll(implGen, populationGen) {
    (implementation, pop) =>
      val originalSize = pop.size
      val selectedSize = implementation.generation(pop).size * 2
      s"Original size: $originalSize, size after selection: $selectedSize" |: Prop.atLeastOne(
        selectedSize == originalSize,
        selectedSize == originalSize - 1
      )
  }
}
