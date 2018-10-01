package genetic

import genetic.genotype.{Fitness, Join, Modification, Scheme}

import scala.language.higherKinds

/**
  * Template of further GeneticAlgorithm implementations providing ability
  * to evolute initial population n times in arbitrary context `F`
  */
trait GeneticAlgorithmTemplate[F[_]] {

  /**
    * Evolves new population of size `populationSize` with set of genetic operators for n iterations
    */
  def createAndEvolve[G: Fitness : Join : Modification : Scheme]
  (populationSize: Int, operators: OperatorSet, iterations: Int): F[Population[G]] =
    evolve(Scheme.make(populationSize), operators, iterations)

  /**
    * Evolves initial population with certain set of genetic operators for n iterations
    */
  def evolve[G: Fitness : Join : Modification](population: Population[G],
                                                    operators: OperatorSet,
                                                    iterations: Int): F[Population[G]]
}

