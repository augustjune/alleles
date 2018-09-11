package genetic.genotype

import genetic.Population

trait GenotypeSyntax {
  implicit class FitnessObj[G](g: G)(implicit f: Fitness[G]) {
    def fitness: Int = f.value(g)
  }

  implicit class RandomChangeObj[G](g: G)(implicit r: RandomChange[G]) {
    def mutated: G = r.modify(g)
  }

  implicit class DesignObj[G](a: G)(implicit d: Design[G]) {
    def populate(n: Int): Population[G] = Design.fromOne(a).make(n)
  }
}
