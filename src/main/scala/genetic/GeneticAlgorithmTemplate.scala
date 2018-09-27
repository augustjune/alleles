package genetic

import cats.kernel.Semigroup
import genetic.genotype.{Fitness, Modification, Scheme}

import scala.language.higherKinds

trait GeneticAlgorithmTemplate[F[_]] {

  def createAndEvolve[G: Fitness : Semigroup : Modification : Scheme]
  (populationSize: Int, operators: OperatorSet, iterations: Int): F[Population[G]] =
    evolve(Scheme.make(populationSize), operators, iterations)

  def evolve[G: Fitness : Semigroup : Modification](population: Population[G],
                                                    operators: OperatorSet,
                                                    iterations: Int): F[Population[G]]
}

