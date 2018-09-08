package genetic.operators.mutation

import genetic._
import genetic.operators.Mutation

import scala.annotation.tailrec
import scala.util.Random

case class ComplexMutation(chance: Double, complexity: Double) extends Mutation {
  def apply(population: Population): Population = population.map(
    if (Random.shot(chance)) mutateSingleChromosome(complexity)
    else identity
  )

  //ToDo - rename
  @tailrec
  private def mutateSingleChromosome(complexity: Double)(permutation: Genotype): Genotype = {
    val mutated = permutation.mutate
    if (Random.shot(complexity)) mutateSingleChromosome(complexity)(mutated)
    else mutated
  }

  override def toString: String = s"ComplexMutation with single permutation mutation chance $chance and mutation complexity $complexity"
}
