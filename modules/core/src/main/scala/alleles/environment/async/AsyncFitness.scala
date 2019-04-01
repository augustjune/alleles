package alleles.environment.async

import scala.concurrent.Future
// ToDo - add documentation
trait AsyncFitness[A] {
  def value(a: A): Future[Double]
}

object AsyncFitness {
  def apply[A](a: A)(implicit f: AsyncFitness[A]): Future[Double] = f.value(a)
}
