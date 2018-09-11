import scala.util.Random

package object genetic {
  type Population[A] = List[A]

  implicit class RandomExtension(rand: Random) {
    /**
      * Returns a random boolean with custom probability
      * @param chance probability of true
      */
    def shot(chance: Double): Boolean = rand.nextDouble() < chance

    /**
      * Returns a value from the `pool` chosen by corresponding chance
      * Note: all chances should sum to 1
      * @param pool sequence of tuples representing value and corresponding chance to be chosen
      */
    def choose[A](pool: Seq[(A, Double)]): A = choose(Random.nextDouble(), pool.toList)

    private def choose[A](shot: Double, pool: List[(A, Double)]): A = pool match {
      case Nil => throw new RuntimeException("Pool chances should sum to 1")
      case (x, chance) :: t => if (shot < chance) x else choose(shot - chance, t)
    }
  }
}
