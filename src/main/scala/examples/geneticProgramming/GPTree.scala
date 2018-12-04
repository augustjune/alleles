package examples.geneticProgramming

import lazyOr._

sealed trait GPTree {
  /**
    * Replace this tree or any of its subtree with other tree
    *
    * @return Pair which contains this modified tree and rest after inserting other
    */
  def insert(other: GPTree): (GPTree, GPTree)

  /**
    * Switch parts or whole trees between `this` and `other` trees
    *
    * @return Pair of original trees, where each contains part of one another
    */
  def cross(other: GPTree): (GPTree, GPTree)
}

sealed abstract class Leaf extends GPTree {
  def insert(other: GPTree): (GPTree, GPTree) = (other, this)

  def cross(other: GPTree): (GPTree, GPTree) = other.insert(this).reverse
}

sealed abstract class UnaryNode(op: GPTree => GPTree) extends GPTree {
  def a: GPTree

  def insert(other: GPTree): (GPTree, GPTree) = (other, this) >|| (op(other), a) >|| a.insert(other).mapFirst(op)

  def cross(other: GPTree): (GPTree, GPTree) = other.insert(this).reverse >|| other.insert(a).mapSecond(op).reverse
}

sealed abstract class BinaryNode(op: ((GPTree, GPTree) => GPTree)) extends GPTree {
  def a: GPTree

  def b: GPTree

  def insert(other: GPTree): (GPTree, GPTree) = (other, this) >|| (op(a, other), b) >|| (op(other, b), a) >||
    a.insert(other).mapFirst(op(_, b)) >|| b.insert(other).mapFirst(op(a, _))

  def cross(other: GPTree): (GPTree, GPTree) = other.insert(this).reverse >||
    other.cross(a).mapSecond(op(_, b)).reverse >|| other.cross(b).mapSecond(op(a, _)).reverse
}

final case class Variable(name: String) extends Leaf
final case class Value(v: Double) extends Leaf

final case class Sin(a: GPTree) extends UnaryNode(Sin)
final case class Cos(a: GPTree) extends UnaryNode(Cos)

final case class Plus(a: GPTree, b: GPTree) extends BinaryNode(Plus)
final case class Minus(a: GPTree, b: GPTree) extends BinaryNode(Minus)
final case class Multiply(a: GPTree, b: GPTree) extends BinaryNode(Multiply)
final case class Divide(a: GPTree, b: GPTree) extends BinaryNode(Divide)
