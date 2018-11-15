package benchmarking

import cats.Semigroup
import cats.data.Writer

trait Measuring extends App {

  type Measured[R] = Writer[Long, R]

  implicit val measuringAccumulator: Semigroup[Long] = (x, y) => x + y

  def measure[R](f: => R): Measured[R] = {
    val start = System.currentTimeMillis()
    val res = f
    Writer(System.currentTimeMillis() - start, res)
  }
}
