package genetic.operators.mutation

import genetic.{Population, RRandom}
import genetic.operators.Mutation

abstract class GenotypeMutation[G](chance: Double) extends Mutation[G] {
  def apply(pop: Population[G]): Population[G] = for (g <- pop) yield
    if (RRandom.shot(chance)) modifyGenotype(g) else g

  protected def modifyGenotype(g: G): G
}
