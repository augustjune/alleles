package genetic

import cats.Semigroup
import genetic.genotype.{Fitness, Modification}
import genetic.operators._

import scala.concurrent.duration.Duration
import scala.language.higherKinds

trait GeneticAlgorithm[F[_]] {
  /**
    * Evolves initial population with genetic operators for a finite number of iterations
    *
    * @param settings   Set of genetic operators which define iterative cycle
    * @param iterations Number of iterations
    * @return Evolved population
    */
  def evolve[G: Fitness : Semigroup : Modification](settings: AlgoSettings[G], iterations: Int): F[Population[G]] =
    evolve[G, Int](settings)(0, { case (i, _) => i < iterations }, _ + 1)

  /**
    * Evolves initial population with genetic operators until the population
    * reaches certain fitness threshold with at least one representative
    *
    * @param settings         Set of genetic operators which define iterative cycle
    * @param fitnessThreshold Threshold to be crossed
    * @return Evolved population
    */
  def evolveUntilReached[G: Fitness : Semigroup : Modification](settings: AlgoSettings[G], fitnessThreshold: Int): F[Population[G]] =
    evolve[G, Unit](settings)((), { case (_, pop) => Fitness(pop.best) > fitnessThreshold }, identity)

  protected def evolve[G: Fitness : Semigroup : Modification, B](settings: AlgoSettings[G])(start: B, until: (B, Population[G]) => Boolean, click: B => B): F[Population[G]]
}

