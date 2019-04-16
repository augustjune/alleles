package alleles.genotype.standard.seq

import alleles.genotype.Join
import alleles.toolset.{IterablePair, RRandom}

import scala.collection.SeqLike
import scala.collection.generic.CanBuildFrom

/**
  * Set of standard Variation implementation for any SeqLike derivative
  */
object Joins {

  def singlePoint[A, Coll <: SeqLike[A, Coll]]
  (implicit cbf: CanBuildFrom[Coll, A, Coll]): Join[Coll] =
    (a: Coll, b: Coll) => {
      val pivot = RRandom.nextInt(a.length)
      val (a1, a2) = a.splitAt(pivot)
      val (b1, b2) = b.splitAt(pivot)
      new IterablePair[Coll](a1 ++ b2, b1 ++ a2)
    }
}
