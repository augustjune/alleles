package genetic.operators.selection

import genetic.{Population, RRandom}
import genetic.genotype.Fitness
import genetic.operators.SelectionStage

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

  def apply[G: Fitness](roundSize: Int, fittestChance: Double): Tournament[G] = new Tournament(roundSize, fittestChance)

  /*
    OptimizationPoint: create separate class for Tournament with chance
                      1 with overridden apply which takes only min value
 */
  def apply[G: Fitness](roundSize: Int): Tournament[G] = new Tournament(roundSize, 1)
}

class Tournament[G: Fitness](roundSize: Int, fittestChance: Double) extends SelectionStage[G] {
  /*
    OptimizationPoint: compose `shuffle` and `take` to one function to
                       avoid unnecessary shuffling of the rest of sequence
   */
  def apply(population: Population[G]): Population[G] =
    for (_ <- population) yield
      RRandom.chooseByChances(assignChances(RRandom.shuffle(population).take(roundSize)))

  private def assignChances(sample: Population[G]): Population[(G, Double)] =
    sample.sortBy(Fitness(_)).zipWithIndex.map {
      case (x, i) => x -> fittestChance * math.pow(1 - fittestChance, i)
    }

  override def toString: String = s"Tournament selection with tournament size of $roundSize"
}
