package genetic.operators.mutation

import genetic.{Mutator, Population, RandomExtension}
import genetic.operators.Mutation

import scala.util.Random

case class SingleChromosomeMutation[A](chance: Double)(implicit m: Mutator[A]) extends Mutation[A] {
  def apply(population: Population[A]): Population[A] = population.map(
    if (Random.shot(chance)) m.mutate else identity)

  override def toString: String = s"SingleChromosomeMutation of chance $chance"
}
