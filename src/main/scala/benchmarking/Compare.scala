package benchmarking

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import examples.qap.PermutationOps
import genetic.engines.{EvolutionEngine, EvolutionOptions, GeneticAlgorithm}
import genetic.genotype.{Fitness, Join, Modification, Scheme}
import genetic.genotype.syntax._
import genetic.operators.crossover.ParentsOrOffspring
import genetic.operators.mutation.RepetitiveMutation
import genetic.operators.selection.Tournament
import genetic._

import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.concurrent.{Await, ExecutionContextExecutor}

object Compare extends App with Measuring {

  def printMeasure[G: Fitness](label: String, time: Long, pop: Population[G]): Unit = {
    println(f"$label%-30s : Time: $time with size: ${pop.size} and best candidate: ${pop.best.fitness}")
  }

  val implicits = new PermutationOps("http://anjos.mgi.polymtl.ca/qaplib/data.d/had20.dat")

  import implicits._

  val initialPopSize = 50000
  val initialPop = Scheme.make(initialPopSize)
  val operators = OperatorSet(
    Tournament(20),
    ParentsOrOffspring(0.25),
    RepetitiveMutation(0.8, 0.5))

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ex: ExecutionContextExecutor = system.dispatcher

  val options = EvolutionOptions(initialPop, operators)
  val iterations = 20

  case class BenchmarkPreferences[G](options: EvolutionOptions[G], iterations: Int)

  def measureEngines[G: Fitness : Join : Modification](labeledEngines: List[(String, EvolutionEngine)],
                                                       preferences: BenchmarkPreferences[G],
                                                       restTime: FiniteDuration = Duration.Zero,
                                                       seed: Long = 0L): List[(String, Measured[Population[G]])] = {
    labeledEngines.map { case (label, engine) =>
      RRandom.setSeed(seed)
      val measuredPop = measure(Await.result(
        engine.evolve(preferences.options).take(preferences.iterations).runWith(Sink.last),
        Duration.Inf))
      Thread.sleep(restTime.toMillis)
      (label, measuredPop)
    }
  }

  def measureN(list: List[(String, EvolutionEngine)]): Unit = {
    for ((label, engine) <- list) {
      RRandom.setSeed(0L)
      println("Measuring: " + label)
      measure(Await.result(engine.evolve(options).take(iterations).runWith(Sink.last), Duration.Inf)).run match {
        case (time, res) => printMeasure(label, time, res)
      }

      Thread.sleep(3000)
    }
  }

  measureN(List(
    "Basic sync" -> GeneticAlgorithm,
    "Fully parallel" -> GeneticAlgorithm.par,
    "Parallel fitness" -> GeneticAlgorithm.parFitness
  ))

  println("End")

  system.terminate()
}
