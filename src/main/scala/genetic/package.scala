import scala.util.Random

package object genetic {
  implicit class RandomExtension(rand: Random) {
    def shot(chance: Double): Boolean = rand.nextDouble() < chance

    //ToDo - rename method
    def shot(chances: Seq[Double]): Int = {
      def iter(chances: List[Double], i: Int, shot: Double): Int = chances match {
        case Nil => throw new RuntimeException
        case h :: t => if (shot <= h) i else iter(t, i + 1, shot - h)
      }
      iter(chances.sorted.reverse.toList, 0, Random.nextDouble())
    }
  }

  type Population = List[Permutation]

  //ToDo - implement
  class Permutation() extends Ordered[Permutation] {
    def fitnessValue: Int = ???
    def mutate: Permutation = ???
    def randomCrossOver(other: Permutation): Permutation = ???

    def compare(other: Permutation): Int = fitnessValue.compare(other.fitnessValue)
  }
}
