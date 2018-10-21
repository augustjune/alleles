package examples.geneticProgramming

import lazyOr._


sealed trait FunTree {
  /**
    * Replace this tree or any of its subtree with other tree
    *
    * @return Pair which contains this modified tree and rest after inserting other
    */
  def insert(other: FunTree): (FunTree, FunTree)

  /**
    * Switch parts or whole trees between `this` and `other` trees
    *
    * @return Pair of original trees, where each contains part of one another
    */
  def cross(other: FunTree): (FunTree, FunTree)

  /**
    * Insert new random tree instead of `this` tree or any of its subtree
    *
    * @return
    */
  def modify(): FunTree
}

final case class Variable(name: String) extends FunTree {
  def insert(other: FunTree): (FunTree, FunTree) = (other, this)

  def cross(other: FunTree): (FunTree, FunTree) = insert(other) >|| other.insert(this).reverse

  def modify(): FunTree = ???
}

final case class Value(v: Double) extends FunTree {
  def insert(other: FunTree): (FunTree, FunTree) = (other, this)

  def cross(other: FunTree): (FunTree, FunTree) = other.insert(this).reverse

  def modify(): FunTree = ???
}

final case class Sin(a: FunTree) extends FunTree {
  def insert(other: FunTree): (FunTree, FunTree) = (other, this) >|| (Sin(other), a) >|| a.insert(other).mapFirst(Sin)

  def cross(other: FunTree): (FunTree, FunTree) = other.insert(this).reverse >|| other.insert(a).mapSecond(Sin).reverse

  def modify(): FunTree = ???
}

final case class Cos(a: FunTree) extends FunTree {
  def insert(other: FunTree): (FunTree, FunTree) = (other, this) >|| (Cos(other), a) >|| a.insert(other).mapFirst(Cos)

  def cross(other: FunTree): (FunTree, FunTree) = other.insert(this).reverse >|| other.insert(a).mapSecond(Cos).reverse

  def modify(): FunTree = ???
}

final case class Plus(a: FunTree, b: FunTree) extends FunTree {
  def insert(other: FunTree): (FunTree, FunTree) = (other, this) >|| (Plus(a, other), b) >|| (Plus(other, b), a) >||
    a.insert(other).mapFirst(Plus(_, b)) >|| b.insert(other).mapFirst(Plus(a, _))

  def cross(other: FunTree): (FunTree, FunTree) = other.insert(this).reverse >||
    other.cross(a).mapSecond(Plus(_, b)).reverse >|| other.cross(b).mapSecond(Plus(a, _)).reverse

  def modify(): FunTree = ???
}

final case class Minus(a: FunTree, b: FunTree) extends FunTree {
  def insert(other: FunTree): (FunTree, FunTree) = (other, this) >|| (Minus(a, other), b) >|| (Minus(other, b), a) >||
    a.insert(other).mapFirst(Minus(_, b)) >|| b.insert(other).mapFirst(Minus(a, _))

  def cross(other: FunTree): (FunTree, FunTree) = other.insert(this).reverse >||
    other.cross(a).mapSecond(Minus(_, b)).reverse >|| other.cross(b).mapSecond(Minus(a, _)).reverse

  def modify(): FunTree = ???
}

final case class Multiply(a: FunTree, b: FunTree) extends FunTree {
  def insert(other: FunTree): (FunTree, FunTree) = (other, this) >|| (Multiply(a, other), b) >|| (Multiply(other, b), a) >||
    a.insert(other).mapFirst(Multiply(_, b)) >|| b.insert(other).mapFirst(Multiply(a, _))

  def cross(other: FunTree): (FunTree, FunTree) = ???

  def modify(): FunTree = ???
}

final case class Divide(a: FunTree, b: FunTree) extends FunTree {
  def insert(other: FunTree): (FunTree, FunTree) = (other, this) >|| (Divide(a, other), b) >|| (Divide(other, b), a) >||
    a.insert(other).mapFirst(Divide(_, b)) >|| b.insert(other).mapFirst(Divide(a, _))

  def cross(other: FunTree): (FunTree, FunTree) = ???

  def modify(): FunTree = ???
}

/*
abstract class BinaryOp[T <: (FunTree, FunTree) => FunTree](op: T) {
  def insert(base: FunTree, other: FunTree)(arg1: FunTree, arg2: FunTree): (FunTree, FunTree) = {
    (op(other, arg2), arg1) >|| (op(arg1, other), arg2) >||
  }

  def cross(x: FunTree, y: FunTree)(arg1: FunTree, arg2: FunTree)
}*/
