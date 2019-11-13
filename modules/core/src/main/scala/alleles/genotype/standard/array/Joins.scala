package alleles.genotype.standard.array

import alleles.genotype.Join
import alleles.toolset.{IterablePair, RRandom}

import scala.reflect.ClassTag

/**
  * Set of standard Join implementation for Array type
  */
object Joins {

  def singlePoint[A: ClassTag]: Join[Array[A]] =
    (a: Array[A], b: Array[A]) => {
      val pivot = RRandom.nextInt(a.length)
      val (a1, a2) = a.splitAt(pivot)
      val (b1, b2) = b.splitAt(pivot)
      new IterablePair[Array[A]](a1 ++ b2, b1 ++ a2)
    }
}
