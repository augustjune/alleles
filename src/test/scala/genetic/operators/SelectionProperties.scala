package genetic.operators

import genetic.Population
import genetic.genotype.Fitness
import org.scalacheck.{Gen, Prop, Properties}
import org.scalacheck.Prop._


abstract class SelectionProperties(name: String) extends Properties(name + " with Selection props") {
  type G

  implicit def fitness: Fitness[G]

  def populationGen: Gen[Population[G]]

  def implGen: Gen[Selection]

  property("Individuals are selected from the population") = forAll(implGen, populationGen) {
    (implementation, pop) =>
      val (i1, i2) = implementation.single(pop)
      pop.contains(i1) && pop.contains(i2)
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
