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
}

sealed abstract class Const extends FunTree {
  def insert(other: FunTree): (FunTree, FunTree) = (other, this)

  def cross(other: FunTree): (FunTree, FunTree) = other.insert(this).reverse
}

sealed abstract class UnaryOp(op: FunTree => FunTree, a: FunTree) extends FunTree {
  def insert(other: FunTree): (FunTree, FunTree) = (other, this) >|| (op(other), a) >|| a.insert(other).mapFirst(op)

  def cross(other: FunTree): (FunTree, FunTree) = other.insert(this).reverse >|| other.insert(a).mapSecond(op).reverse
}

sealed abstract class BinaryOp(op: (FunTree, FunTree) => FunTree, a: FunTree, b: FunTree) extends FunTree {
  def insert(other: FunTree): (FunTree, FunTree) = (other, this) >|| (op(a, other), b) >|| (op(other, b), a) >||
    a.insert(other).mapFirst(op(_, b)) >|| b.insert(other).mapFirst(op(a, _))

  def cross(other: FunTree): (FunTree, FunTree) = other.insert(this).reverse >||
    other.cross(a).mapSecond(op(_, b)).reverse >|| other.cross(b).mapSecond(op(a, _)).reverse
}

final case class Variable(name: String) extends Const

final case class Value(v: Double) extends Const

final case class Sin(a: FunTree) extends UnaryOp(Sin, a)

final case class Cos(a: FunTree) extends UnaryOp(Cos, a)

final case class Plus(a: FunTree, b: FunTree) extends BinaryOp(Plus, a, b)

final case class Minus(a: FunTree, b: FunTree) extends BinaryOp(Minus, a, b)

final case class Multiply(a: FunTree, b: FunTree) extends BinaryOp(Multiply, a, b)

final case class Divide(a: FunTree, b: FunTree) extends BinaryOp(Divide, a, b)
