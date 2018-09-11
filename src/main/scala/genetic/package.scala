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
      * Returns a value from the 'chancesMap' with corresponding randomized chance
      * @param chancesMap map of chances and their elements with all chances summing to 1
      */
    def shotSeq[A](chancesMap: Map[Double, A]): A = {
      def loop(chances: List[Double], shot: Double): A = chances match {
        case Nil => throw new RuntimeException("All chances should sum to 1")
        case h :: t => if (shot <= h) chancesMap(h) else loop(t, shot - h)
      }
      loop(chancesMap.keys.toList.sorted.reverse, Random.nextDouble())
    }
  }
}
