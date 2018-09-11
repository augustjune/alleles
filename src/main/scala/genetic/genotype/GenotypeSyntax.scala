package genetic.genotype

import genetic.Population

trait GenotypeSyntax {
  implicit class FitnessObj[A](a: A)(implicit f: Fitness[A]) {
    def fitness: Int = f.value(a)
  }

  implicit class RandomChangeObj[A](a: A)(implicit r: RandomChange[A]) {
    def mutated: A = r.modify(a)
  }

  implicit class DesignObj[A](a: A)(implicit d: Design[A]) {
    def populate(n: Int): Population[A] = Design.fromOne(a).make(n)
  }
}
