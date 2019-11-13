package alleles.genotype.standard.seq

import alleles.genotype.Join
import alleles.toolset.{IterablePair, RRandom}

import scala.collection.IterableOps

/**
  * Set of standard Variation implementation for any SeqLike derivative
  */
object Joins {

  def singlePoint[A, Coll[x] <: IterableOps[x, Coll, Coll[x]]]: Join[Coll[A]] =
    (a: Coll[A], b: Coll[A]) => {
      val pivot = RRandom.nextInt(a.size)
      val (a1, a2) = a.splitAt(pivot)
      val (b1, b2) = b.splitAt(pivot)
      new IterablePair[Coll[A]](a1 ++ b2, b1 ++ a2)
    }

}
