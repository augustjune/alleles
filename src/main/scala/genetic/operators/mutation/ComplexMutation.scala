package genetic.operators.mutation

import genetic._
import genetic.genotype.RandomChange

import scala.annotation.tailrec
import scala.util.Random

case class ComplexMutation[G: RandomChange](chance: Double, complexity: Double) extends GenotypeMutation[G](chance) {

  @tailrec
  final protected def modifyGenotype(a: G): G = {
    val mutated = RandomChange(a)
    if (Random.shot(complexity)) modifyGenotype(mutated)
    else mutated
  }

  override def toString: String = s"ComplexMutation with single genotype mutation chance $chance and mutation complexity $complexity"
}
