package genetic

import scala.concurrent.Future

package object rebasedGA {
  type PopulationWithBest[G] = (Population[G], (G, Double))

  trait AsyncFitness[G] {
    def value(g: G): Future[Double]
  }

  object AsyncFitness {
    def apply[G](g: G)(implicit f: AsyncFitness[G]): Future[Double] = f.value(g)
  }
}
