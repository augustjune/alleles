package alleles.stages.selection

import alleles.Population
import alleles.genotype.Fitness.Rated
import alleles.stages.Selection
import alleles.toolset.RRandom

/**
  * Selecting an individual from a randomly taken sample of population sorted by fitness values and
  * probability of taking each genotype of sample: p*((1-p)^^i) where 'i' is index in sorted sample
  *
  * Example - tournament of round size 3:
  * Population with corresponding fitness values
  * x1 -> 6
  * x2 -> 10
  * x3 -> 12
  * x4 -> 3
  * x5 -> 5
  *
  * Randomly taken sample in round: (x2, x4, x5)
  * One individual is going to be taken to the next pop with corresponding chances:
  * x4 -> p
  * x5 -> p * (1 - p)
  * x2 -> p * (1 - p) * (1 - p)
  *
  * Repeat rounds until new population is not filled
  */
private[stages] class Tournament(roundSize: Int, fittestChance: Double) extends Selection {
  def pair[A](pop: Population[Rated[A]]): (A, A) = (choose(pop), choose(pop))

  private def choose[A](pop: Population[Rated[A]]) =
    RRandom.chooseByChances(assignChances(RRandom.take(roundSize, pop)))

  private def assignChances[A](sample: Population[Rated[A]]): Population[(A, Double)] =
    sample.sortBy(_._2).zipWithIndex.map {
      case ((x, _), i) => x -> fittestChance * math.pow(1 - fittestChance, i)
    }

  def generation[A](pop: Population[Rated[A]]): Population[(A, A)] =
    for (_ <- (1 to pop.size / 2).toVector) yield pair(pop)

  override def toString: String = s"Tournament(roundSize: $roundSize, fittestChance: $fittestChance)"
}
