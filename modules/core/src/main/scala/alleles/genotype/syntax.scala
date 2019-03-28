package alleles.genotype

import alleles.Population
import alleles.toolset.IterablePair

/**
  * Set of implicit value classes, which enable neat type classes' function extensions
  */
object syntax {
  implicit class FitnessObj[A](private val ind: A) extends AnyVal {
    def fitness(implicit f: Fitness[A]): Double = f.value(ind)
  }

  implicit class JoinObj[A](private val ind: A) extends AnyVal {
    def ><(other: A)(implicit join: Join[A]): IterablePair[A] = join.cross(ind, other)
  }

  implicit class ModifiableObj[A](private val ind: A) extends AnyVal {
    def modified(implicit c: Variation[A]): A = c.modify(ind)
  }

  implicit class SchemeObj[A](private val ind: A) extends AnyVal {
    def populate(n: Int): Population[A] = Scheme.fromOne(ind).make(n)
  }
}
