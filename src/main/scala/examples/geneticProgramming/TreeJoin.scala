package examples.geneticProgramming

import genetic.collections.IterablePair
import genetic.genotype.Join
import lazyOr._

object TreeJoin extends Join[FunTree] {

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
      case Sin(a) => insert(x, y) >|| crossPair(x, a).mapSecond(Sin)// crossOne(Sin, a)
      case Cos(a) => insert(x, y) >|| crossPair(x, a).mapSecond(Cos)
      case Plus(a, b) => insert(x,y) >|| crossPair(x, a).mapSecond(Plus(_, a)) >|| crossPair(x, b).mapSecond(Plus(a, _))   // crossTwo(Plus, a, b)
      case Minus(a, b) => insert(x,y) >|| crossPair(x, a).mapSecond(Minus(_, a)) >|| crossPair(x, b).mapSecond(Minus(a, _))
      case Multiply(a, b) => insert(x,y) >|| crossPair(x, a).mapSecond(Multiply(_, a)) >|| crossPair(x, b).mapSecond(Multiply(a, _))
      case Divide(a, b) => insert(x,y) >|| crossPair(x, a).mapSecond(Divide(_, a)) >|| crossPair(x, b).mapSecond(Divide(a, _))
    }
  }

  def cross(x: FunTree, y: FunTree): IterablePair[FunTree] = crossPair(x, y).map(IterablePair.apply)
}
