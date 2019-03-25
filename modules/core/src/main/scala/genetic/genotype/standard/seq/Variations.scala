package genetic.genotype.standard.seq

import genetic.genotype.Variation
import genetic.toolset.RRandom
import genetic.genotype.syntax._

object Variations {
  def swap[A]: Variation[Seq[A]] =
    (ind: Seq[A]) => {
      // ToDo - handle same indices and individuals of size < 2
      val i1, i2 = RRandom.nextInt(ind.length)
      val temp = ind(i1)
      ind.updated(i1, ind(i2)).updated(i2, temp)
    }

  def elementVariation[A: Variation]: Variation[Seq[A]] =
    (ind: Seq[A]) => {
      val i = RRandom.nextInt(ind.length)
      ind.updated(i, ind(i).modified)
    }
}
