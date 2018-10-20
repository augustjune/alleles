package examples.geneticProgramming

sealed trait FunTree

final case class Variable(name: String) extends FunTree

final case class Value(v: Double) extends FunTree

final case class Sin(a: FunTree) extends FunTree

final case class Cos(a: FunTree) extends FunTree

final case class Plus(a: FunTree, b: FunTree) extends FunTree

final case class Minus(a: FunTree, b: FunTree) extends FunTree

final case class Multiply(a: FunTree, b: FunTree) extends FunTree

final case class Divide(a: FunTree, b: FunTree) extends FunTree
