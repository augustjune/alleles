package genetic.operators.mutation

import genetic.{Population, RandomExtension}
import genetic.operators.Mutation

import scala.util.Random

case class SingleChromosomeMutation(chance: Double) extends Mutation {
  def apply(population: Population): Population = population.map(
    if (Random.shot(chance)) _.mutate else identity)

  override def toString: String = s"SingleChromosomeMutation of chance $chance"
}
