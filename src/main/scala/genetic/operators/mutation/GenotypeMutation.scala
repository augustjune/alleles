package genetic.operators.mutation

import genetic.{Population, RandomExtension}
import genetic.operators.Mutation

import scala.util.Random

abstract class GenotypeMutation[G](chance: Double) extends Mutation[G] {
  def apply(pop: Population[G]): Population[G] = for (g <- pop) yield
    if (Random.shot(chance)) modifyGenotype(g) else g

  protected def modifyGenotype(g: G): G
}
