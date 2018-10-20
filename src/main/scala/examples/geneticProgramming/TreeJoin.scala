package examples.geneticProgramming

import genetic.collections.IterablePair
import genetic.genotype.Join
import lazyOr._

object TreeJoin extends Join[FunTree] {

  implicit class MapTuple[T1, T2](private val tuple: (T1, T2)) extends AnyVal {
    def mapFirst[V](f: T1 => V): (V, T2) = tuple match {
      case (x, y) => (f(x), y)
    }

    def mapSecond[V](f: T2 => V): (T1, V) = tuple match {
      case (x, y) => (x, f(y))
    }

    def map[V](f: (T1, T2) => V): V = f(tuple._1, tuple._2)
  }


  // (new, left)
  def insert(base: FunTree, other: FunTree): (FunTree, FunTree) = {
    def insert1(ap: FunTree => FunTree, a: FunTree): (FunTree, FunTree) =
      (ap(other), a) >|| insert(a, other).mapFirst(ap)

    def insert2(ap: (FunTree, FunTree) => FunTree, a: FunTree, b: FunTree): (FunTree, FunTree) =
      (ap(other, b), a) >|| (ap(a, other), b) >|| insert(a, other).mapFirst(ap(_, b)) >|| insert(b, other).mapFirst(ap(a, _))

    base match {
      case Variable(_) | Value(_) => (other, base)
      case Sin(a) => insert1(Sin, a)
      case Cos(a) => insert1(Cos, a)
      case Plus(a, b) => insert2(Plus, a, b)
      case Minus(a, b) => insert2(Minus, a, b)
      case Multiply(a, b) => insert2(Multiply, a, b)
      case Divide(a, b) => insert2(Divide, a, b)
    }
  }

  def crossPair(x: FunTree, y: FunTree): (FunTree, FunTree) = {
    def crossOne(ap: FunTree => FunTree, a: FunTree) =
      insert(x, y) >|| crossPair(x, a).mapSecond(ap)

    def crossTwo(ap: (FunTree, FunTree) => FunTree, a: FunTree, b: FunTree) =
      insert(x, y) >|| crossPair(x, a).mapSecond(ap(_, b)) >|| crossPair(x, b).mapSecond(ap(a, _))

    y match {
      case Variable(_) | Value(_) => insert(x, y)
      case Sin(a) => crossOne(Sin, a)
      case Cos(a) => crossOne(Cos, a)
      case Plus(a, b) => crossTwo(Plus, a, b)
      case Minus(a, b) => crossTwo(Minus, a, b)
      case Multiply(a, b) => crossTwo(Multiply, a, b)
      case Divide(a, b) => crossTwo(Divide, a, b)
    }
  }

  def cross(x: FunTree, y: FunTree): IterablePair[FunTree] = crossPair(x, y).map(IterablePair.apply)
}
