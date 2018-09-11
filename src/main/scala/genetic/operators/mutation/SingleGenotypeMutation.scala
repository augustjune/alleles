package genetic.operators.mutation

import genetic.genotype.RandomChange


case class SingleGenotypeMutation[G: RandomChange](chance: Double) extends GenotypeMutation[G](chance) {
  protected def modifyGenotype(g: G): G = RandomChange(g)

  override def toString: String = s"SingleChromosomeMutation of chance $chance"
}
