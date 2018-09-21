package genetic.engines

import cats.Semigroup
import genetic.genotype.{Fitness, Modification}
import genetic.{AlgoSettings, GeneticAlgorithm, Population}

import scala.concurrent.duration.Duration
import scala.language.higherKinds

trait SynchronousGA[F[_]] extends GeneticAlgorithm[F] {
  /**
    * Evolves initial population with genetic operators for a provided time
    * Warning: is nondeterministic because of varying time of performance of a certain operation by CPU,
    * thus can not be exactly reproduced with ReusableRandom
    *
    * @param settings Set of genetic operators which define iterative cycle
    * @param duration Duration of evolving
    * @return Evolved population
    */
  def evolve[G: Fitness : Semigroup : Modification](settings: AlgoSettings[G], duration: Duration): F[Population[G]] = {
    val start = System.currentTimeMillis()
    evolve[G, Long](settings)(start, {case (time, _) => time < start + duration.toMillis}, _ => System.currentTimeMillis())
  }
}
