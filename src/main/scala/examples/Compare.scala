package examples

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import examples.qap.{Permutation, PermutationOps}
import genetic.genotype.{Fitness, Join, Modification, Scheme}
import genetic.genotype.syntax._
import genetic.operators.crossover.ParentsOrOffspring
import genetic.operators.mutation.RepetitiveMutation
import genetic.operators.selection.Tournament
import genetic.{OperatorSet, Population, PopulationExtension, RRandom}
import genetic.engines.streaming._
import genetic.engines.sync.{ParallelGA, SequentialGA, SynchronousGA}
import genetic.rebasedGA.ParallelEvolutionEngine

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor}

object Compare extends App {

  def measureN[R: Fitness](list: List[(String, () => Population[R])]): Unit = {
    for ((label, popF) <- list) {
      RRandom.setSeed(0L)
      println("Measuring: " + label)
      val (time, res) = measure(popF())
      printMeasure(label, time, res)
    }
  }


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


  val iterations = 15

  object GeneticAlgorithm extends SynchronousGA {
    def par: SynchronousGA = ParallelGA

    def evolutionStep[G: Fitness : Join : Modification](population: Population[G], operators: OperatorSet): Population[G] =
      SequentialGA.evolutionStep(population, operators)
  }

  measureN(List(
    ("Basic sync", () => SequentialGA.evolve(initialPop, operators, iterations)),
    ("Basic sync2", () => SequentialGA.evolve(initialPop, operators, iterations)),
    ("Parallel sync", () => ParallelGA.evolve(initialPop, operators, iterations)),
    ("Streaming GA", () => Await.result(ParallelGA.stream.evolve(initialPop, operators, iterations + 1).runWith(Sink.last), Duration.Inf)),
    ("Rebased Streaming Parallel GA", () => Await.result(ParallelEvolutionEngine.evolve(initialPop, operators).take(iterations + 1).runWith(Sink.last), Duration.Inf))
  ))

  println(Await.result(ParallelEvolutionEngine.evolve(initialPop, operators).take(iterations + 1).map(_.take(10)).runWith(Sink.foreach(println)), Duration.Inf))

  system.terminate()
}
