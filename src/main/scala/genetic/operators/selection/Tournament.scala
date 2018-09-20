package genetic.operators.selection

import genetic.genotype.Fitness
import genetic.operators.Selection
import genetic.{Population, RRandom}

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
object Tournament {
  def apply[G: Fitness](roundSize: Int, fittestChance: Double): Tournament = new Tournament(roundSize, fittestChance)

  /*
    OptimizationPoint: create separate class for Tournament with chance
                      1 with overridden apply which takes only min value
 */
  def apply[G: Fitness](roundSize: Int): Tournament = new Tournament(roundSize, 1)
}

class Tournament(roundSize: Int, fittestChance: Double) extends Selection {
  def apply[G: Fitness](pop: Population[G]): (G, G) = (choose(pop), choose(pop))

  /*
    OptimizationPoint: compose `shuffle` and `take` to one function to
                       avoid unnecessary shuffling of the rest of sequence
   */
  private def choose[G: Fitness](pop: Population[G]) =
    RRandom.chooseByChances(assignChances(RRandom.shuffle(pop).take(roundSize)))

  private def assignChances[G: Fitness](sample: Population[G]): Population[(G, Double)] =
    sample.sortBy(Fitness(_)).zipWithIndex.map {
      case (x, i) => x -> fittestChance * math.pow(1 - fittestChance, i)
    }

  def expand[G: Fitness]: Population[G] => Population[(G, G)] =
    pop => for (_ <- (1 to pop.size / 2).toList) yield apply(pop)

}
