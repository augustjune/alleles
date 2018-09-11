import genetic.genotype.Fitness

import scala.util.Random

package object genetic {
  type Population[A] = List[A]

  implicit class PopulationExtension[A](population: Population[A]) {
    def best(implicit f: Fitness[A]): A = population.minBy(f.value)
  }

  implicit class RandomExtension(rand: Random) {
    /**
      * Returns a random boolean with custom probability
      * @param chance probability of true
      */
    def shot(chance: Double): Boolean = rand.nextDouble() < chance


    /**
      * Returns a value from the `pool` chosen by corresponding priority
      * @param pool sequence of tuples representing value and corresponding priority
      */
    def chooseByPriorities[A](pool: Seq[(A, Int)]): A = {
      val sum = pool.map(_._2).sum
      chooseByChances(pool.map {case (x, p) => x -> (p.toDouble / sum)})
    }

    /**
      * Returns a value from the `pool` chosen by corresponding chance
      * Note: total sum of all chances must equal 1
      */
    def chooseByChances[A](pool: Seq[(A, Double)]): A = choose(Random.nextDouble(), pool.toList)

    private def choose[A](shot: Double, pool: List[(A, Double)]): A = pool match {
      case Nil => throw new RuntimeException("Pool chances should sum to 1")
      case (x, chance) :: t => if (shot < chance) x else choose(shot - chance, t)
    }
  }
}
