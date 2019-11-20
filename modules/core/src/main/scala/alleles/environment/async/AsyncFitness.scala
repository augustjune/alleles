package alleles.environment.async

/**
  * Asynchronous version of fitness function
  */
trait AsyncFitness[F[_], A] {
  def value(a: A): F[Double]
}

object AsyncFitness {
  def apply[F[_], A](a: A)(implicit f: AsyncFitness[F, A]): F[Double] = f.value(a)
}
