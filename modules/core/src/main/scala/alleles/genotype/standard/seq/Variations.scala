package alleles.genotype.standard.seq

import alleles.genotype.Variation
import alleles.genotype.syntax._
import alleles.toolset.RRandom

import scala.collection.SeqOps

/**
  * Set of standard Variation implementation for any SeqLike derivative
  */
object Variations {

  /**
    * Makes sense only for individuals of size greater than 1
    */
  def swap[A, Coll[_] <: SeqOps[A, Coll, Coll[A]]]: Variation[Coll[A]] =
    (ind: Coll[A]) => if (ind.size < 2) ind
    else {
      val i1 = RRandom.nextInt(ind.size)
      var i2 = RRandom.nextInt(ind.size - 1)
      if (i1 == i2) i2 = ind.size - 1
      ind.updated(i1, ind(i2)).updated(i2, ind(i1))
    }

  def elementVariation[A: Variation, Coll[_] <: SeqOps[A, Coll, Coll[A]]]: Variation[Coll[A]] =
    (ind: Coll[A]) => {
      val i = RRandom.nextInt(ind.size)
      ind.updated(i, ind(i).modified)
    }
}
