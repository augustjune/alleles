package genetic

import scala.concurrent.Future

package object rebasedGA {
  type PopulationWithBest[G] = (Population[G], (G, Double))

  /*
  trait Fitness[G] {
  def value(g: G): Double

  def cached: CachedFitness[G] = new CachedFitness[G](this)
}

object Fitness {
  /**
    * Applies trait function from the implicit scope;
    * allows to use trait as context bounds
    */
  def apply[G](g: G)(implicit f: Fitness[G]): Double = f.value(g)
}
   */

  trait AsyncFitness[G] {
    def value(g: G): Future[Double]
  }

  object AsyncFitness {
    def apply[G](g: G)(implicit f: AsyncFitness[G]): Future[Double] = f.value(g)
  }
}
