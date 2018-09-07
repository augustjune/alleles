import genetic.operators.{Mixing, Mutation, Same, Selection}

import scala.concurrent.duration.Duration
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

  object GeneticEngine {
    def apply(selection: Selection = Same, mixing: Mixing = Same, mutation: Mutation = Same): GeneticEngine =
      new GeneticEngine(selection, mixing, mutation)
  }

  class GeneticEngine(selection: Selection, mixing: Mixing, mutation: Mutation) {

    def iterate(initial: Population, times: Int): Population = {
      def iter(n: Int, population: Population): Population =
        if (n > times) population
        else iter(n + 1, cycle(population))

      iter(0, initial)
    }

    def iterate(initial: Population, duration: Duration): Population = {
      val end = System.nanoTime() + duration.toNanos

      def iter(population: Population): Population =
        if (System.nanoTime() > end) population
        else iter(cycle(population))

      iter(initial)
    }

    protected def cycle(population: Population): Population = mutation(mixing(selection(population)))
  }
}
