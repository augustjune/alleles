package genetic.genotype

import genetic.Population

object syntax {
  implicit class FitnessObj[G](val g: G) extends AnyVal {
    def fitness(implicit f: Fitness[G]): Int = f.value(g)
  }

  implicit class RandomChangeObj[G](val g: G) extends AnyVal {
    def mutated(implicit c: RandomChange[G]): G = c.modify(g)
  }

  implicit class SchemeObj[G](val g: G) extends AnyVal {
    def populate(n: Int): Population[G] = Scheme.fromOne(g).make(n)
  }
}

