package genetic.operators.mutation

import genetic._
import genetic.operators.Mutation

import scala.annotation.tailrec
import scala.util.Random

case class ComplexMutation[A](chance: Double, complexity: Double)(implicit m: Mutator[A]) extends Mutation[A] {
  def apply(population: Population[A]): Population[A] = population.map(
    if (Random.shot(chance)) mutateSingleChromosome(complexity)
    else identity
  )

  //ToDo - rename
  @tailrec
  private def mutateSingleChromosome(complexity: Double)(a: A): A = {
    val mutated = m.mutate(a)
    if (Random.shot(complexity)) mutateSingleChromosome(complexity)(mutated)
    else mutated
  }

  override def toString: String = s"ComplexMutation with single permutation mutation chance $chance and mutation complexity $complexity"
}
