package genetic.operators.mutation

import genetic._
import genetic.genotype.RandomChange

import scala.annotation.tailrec

object ComplexMutation {
  def apply[G: RandomChange](chance: Double, complexity: Double): ComplexMutation[G] = new ComplexMutation(chance, complexity)
}

class ComplexMutation[G: RandomChange](chance: Double, complexity: Double) extends GenotypeMutation[G](chance) {

  @tailrec
  final protected def modifyGenotype(g: G): G = {
    val mutated = RandomChange(g)
    if (RRandom.shot(complexity)) modifyGenotype(mutated)
    else mutated
  }

  override def toString: String = s"ComplexMutation with single genotype mutation chance $chance and mutation complexity $complexity"
}
