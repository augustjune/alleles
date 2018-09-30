package genetic

import genetic.genotype.{Fitness, Join, Modification, Scheme}

import scala.language.higherKinds

trait GeneticAlgorithmTemplate[F[_]] {

  def createAndEvolve[G: Fitness : Join : Modification : Scheme]
  (populationSize: Int, operators: OperatorSet, iterations: Int): F[Population[G]] =
    evolve(Scheme.make(populationSize), operators, iterations)

  def evolve[G: Fitness : Join : Modification](population: Population[G],
                                                    operators: OperatorSet,
                                                    iterations: Int): F[Population[G]]
}

