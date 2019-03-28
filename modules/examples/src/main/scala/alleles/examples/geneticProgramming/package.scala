package alleles.examples

import alleles.genotype.{Scheme, Variation}

package object geneticProgramming {

  implicit class MapTuple[T1, T2](private val tuple: (T1, T2)) extends AnyVal {
    def mapFirst[V](f: T1 => V): (V, T2) = tuple match {
      case (x, y) => (f(x), y)
    }

    def mapSecond[V](f: T2 => V): (T1, V) = tuple match {
      case (x, y) => (x, f(y))
    }

    def mapBoth[V1, V2](f1: T1 => V1, f2: T2 => V2): (V1, V2) = tuple match {
      case (t1, t2) => (f1(t1), f2(t2))
    }

    def reverse: (T2, T1) = tuple match {
      case (x, y) => (y, x)
    }
  }

  class TreeVariation(generator: TreeGen) extends Variation[GPTree] {
    def modify(g: GPTree): GPTree = g.insert(generator.randomTree())._1
  }

  class TreeScheme(generator: TreeGen) extends Scheme[GPTree] {
    def create(): GPTree = generator.randomTree()
  }
}
