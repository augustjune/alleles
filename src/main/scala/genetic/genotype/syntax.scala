package genetic.genotype

import genetic.Population
import genetic.collections.IterablePair

/**
  * Set of implicit value classes, which enable neat type classes' function extensions
  */
object syntax {
  implicit class FitnessObj[G](private val g: G) extends AnyVal {
    def fitness(implicit f: Fitness[G]): Double = f.value(g)
  }

  implicit class JoinObj[G](private val g: G) extends AnyVal {
    def ><(other: G)(implicit join: Join[G]): IterablePair[G] = join.cross(g, other)
  }

  implicit class ModifiableObj[G](private val g: G) extends AnyVal {
    def modified(implicit c: Modification[G]): G = c.modify(g)
  }

  implicit class SchemeObj[G](private val g: G) extends AnyVal {
    def populate(n: Int): Population[G] = Scheme.fromOne(g).make(n)
  }
}
