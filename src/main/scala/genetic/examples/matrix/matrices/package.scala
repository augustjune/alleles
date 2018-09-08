package genetic.examples.matrix

package object matrices {

  class Matrix[V](values: Vector[Vector[V]]) extends ((Int, Int) => V) {
    def apply(row: Int, column: Int): V = values(row)(column)

    override def toString: String = values.map(_.mkString(" ")).mkString("\n")
  }

  class SquareMatrix[V](values: Vector[Vector[V]]) extends Matrix(values: Vector[Vector[V]]) {
    require(values.forall(_.size == values.size))

    lazy val size: Int = values.size

    override def toString: String =
      values.map(_.mkString(" ")).mkString("\n")
  }

  type FlowMatrix = SquareMatrix[Int]
  type RangeMatrix = SquareMatrix[Int]
}
