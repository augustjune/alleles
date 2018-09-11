package genetic.operators.mutation

import genetic.genotype.RandomChange
import genetic.{Population, RandomExtension}
import genetic.operators.Mutation

import scala.util.Random

case class SingleChromosomeMutation[A: RandomChange](chance: Double) extends Mutation[A] {
  def apply(population: Population[A]): Population[A] = population.map(a =>
    if (Random.shot(chance)) RandomChange(a) else a)

  override def toString: String = s"SingleChromosomeMutation of chance $chance"
}
