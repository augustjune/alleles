package benchmarking

import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import benchmarking.Measuring.Measured
import genetic.engines.Setting
import genetic.genotype.{Fitness, Join, Variation}
import genetic.Population
import genetic.toolset.RRandom

import scala.concurrent.Await
import scala.concurrent.duration._

trait LongRunningComparison[A, B] extends Measuring {

  def candidates: List[(String, Setting)]

  val evolutionPreferences: EvolutionPreferences[A]

  val seed: Long = RRandom.seed

  val restTime: FiniteDuration = new FiniteDuration(0, MILLISECONDS)

  val resultMapper: ((String, Measured[Population[A]]) => B)

  def run(implicit mat: ActorMaterializer, f: Fitness[A], j: Join[A], m: Variation[A]): List[B] = {
    candidates.map { case (label, engine) =>
      RRandom.setSeed(seed)
      println("Measuring: " + label)
      val res = measure(Await.result(
        engine.evolve(evolutionPreferences.options)
          .take(evolutionPreferences.iterations)
          .runWith(Sink.last),
        Duration.Inf
      ))
      Thread.sleep(restTime.toMillis)
      resultMapper(label, res)
    }
  }
}
