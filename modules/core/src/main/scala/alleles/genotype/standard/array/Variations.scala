package alleles.genotype.standard.array

import alleles.genotype.Variation
import alleles.genotype.syntax._
import alleles.toolset.RRandom

import scala.collection.generic.CanBuildFrom

/**
  * Set of standard Variation implementation for Array type
  */

object Variations {

  // ToDo - think about modifying the arrays

  /**
    * Makes sense only for individuals of size greater than 1
    */
  def swap[A](implicit cbf: CanBuildFrom[Array[_], A, Array[A]]): Variation[Array[A]] =
    (ind: Array[A]) =>
      if (ind.length < 2) ind
      else {
        val i1 = RRandom.nextInt(ind.length)
        var i2 = RRandom.nextInt(ind.length - 1)
        if (i1 == i2) i2 = ind.length - 1
        ind.updated(i1, ind(i2)).updated(i2, ind(i1))
      }

  def elementVariation[A](implicit cbf: CanBuildFrom[Array[_], A, Array[A]],
                          v: Variation[A]): Variation[Array[A]] =
    (ind: Array[A]) => {
      val i = RRandom.nextInt(ind.length)
      ind.updated(i, ind(i).modified)
    }

}
