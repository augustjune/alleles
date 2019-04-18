package alleles.environment

import alleles.Population
import alleles.genotype.Fitness.Rated

trait Ranking[A] {
  def rate(population: Population[A]): Population[Rated[A]]
}
