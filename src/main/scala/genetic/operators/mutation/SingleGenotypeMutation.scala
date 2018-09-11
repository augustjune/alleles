package genetic.operators.mutation

import genetic.genotype.RandomChange

object SingleGenotypeMutation {
  def apply[G: RandomChange](chance: Double): SingleGenotypeMutation[G] = new SingleGenotypeMutation(chance)
}

class SingleGenotypeMutation[G: RandomChange](chance: Double) extends ComplexMutation[G](chance, 0) {
  override def toString: String = s"SingleChromosomeMutation of chance $chance"
}
