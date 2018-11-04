package examples

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import examples.qap.PermutationOps
import genetic.engines.{EvolutionEngine, EvolutionOptions, GeneticAlgorithm}
import genetic.genotype.syntax._
import genetic.genotype.{Fitness, Join, Modification, Scheme}
import genetic.operators.crossover.ParentsOrOffspring
import genetic.operators.mutation.RepetitiveMutation
import genetic.operators.selection.Tournament
import genetic.{OperatorSet, Population, PopulationExtension, RRandom}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor}

object Compare extends App {


  def printMeasure[G: Fitness](label: String, time: Long, pop: Population[G]): Unit = {
    println(f"$label%-30s : Time: $time with size: ${pop.size} and best candidate: ${pop.best.fitness}")
  }

  def measure[R](f: => R): (Long, R) = {
    val start = System.currentTimeMillis()
    val res = f
    (System.currentTimeMillis() - start, res)
  }

  val implicits = new PermutationOps("http://anjos.mgi.polymtl.ca/qaplib/data.d/had20.dat")

  import implicits._

  val initialPopSize = 10000
  val initialPop = Scheme.make(initialPopSize)
  val operators = OperatorSet(
    Tournament(20),
    ParentsOrOffspring(0.25),
    RepetitiveMutation(0.8, 0.5))

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ex: ExecutionContextExecutor = system.dispatcher

  val options = EvolutionOptions(initialPop, operators)


  val iterations = 15

  def measureN(list: List[(String, EvolutionEngine)]): Unit = {
    for ((label, engine) <- list) {
      RRandom.setSeed(0L)
      println("Measuring: " + label)
      val (time, res) = measure(Await.result(engine.evolve(options).take(iterations).runWith(Sink.last), Duration.Inf))
      printMeasure(label, time, res)
    }
  }

  measureN(List(
    "Basic sync" -> GeneticAlgorithm,
    "Parallel sync" -> GeneticAlgorithm.par,
  ))

  system.terminate()
}
