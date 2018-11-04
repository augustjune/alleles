package genetic.engines.async

import scala.concurrent.Future

trait AsyncFitness[G] {
  def value(g: G): Future[Double]
}

object AsyncFitness {
  def apply[G](g: G)(implicit f: AsyncFitness[G]): Future[Double] = f.value(g)
}
