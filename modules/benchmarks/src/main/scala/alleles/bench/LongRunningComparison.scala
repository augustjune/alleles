package alleles.bench

import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import alleles.Population
import alleles.bench.Measuring.Measured
import alleles.environment.Setting
import alleles.genotype.{Fitness, Join, Variation}
import alleles.toolset.RRandom

import scala.concurrent.Await
import scala.concurrent.duration._

trait LongRunningComparison[A, B] extends Measuring {

  def candidates: List[(String, Setting[A])]

  val preferences: EvolutionPreferences[A]

  val seed: Long = RRandom.seed

  val restTime: FiniteDuration = new FiniteDuration(0, MILLISECONDS)

  val resultMapper: ((String, Measured[Population[A]]) => B)

  def run(implicit mat: ActorMaterializer, f: Fitness[A], j: Join[A], m: Variation[A]): List[B] = {
    candidates.map { case (label, engine) =>
      RRandom.setSeed(seed)
      println("Measuring: " + label)
      val res = measure(Await.result(
        engine.evolve(preferences.options)
          .take(preferences.iterations)
          .runWith(Sink.last),
        Duration.Inf
      ))
      Thread.sleep(restTime.toMillis)
      resultMapper(label, res)
    }
  }
}
