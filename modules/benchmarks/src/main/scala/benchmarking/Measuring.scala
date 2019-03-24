package benchmarking

import cats.Semigroup
import cats.data.Writer

object Measuring {
  type Measured[R] = Writer[Long, R]

  implicit val measuringAccumulator: Semigroup[Long] = (x, y) => x + y
}

trait Measuring {
  import Measuring._

  def measure[R](f: => R): Measured[R] = {
    val start = System.currentTimeMillis()
    val res = f
    Writer(System.currentTimeMillis() - start, res)
  }
}
