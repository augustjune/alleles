package alleles.environment.async

import scala.concurrent.Future

/**
  * Asynchronous version of fitness function
  */
trait AsyncFitness[A] {
  def value(a: A): Future[Double]
}

object AsyncFitness {
  def apply[A](a: A)(implicit f: AsyncFitness[A]): Future[Double] = f.value(a)
}
