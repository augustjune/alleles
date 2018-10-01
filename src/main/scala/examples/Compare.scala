package examples

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import examples.matrix.{MatrixImplicits, Permutation}
import genetic.genotype.{Fitness, Join, Modification, Scheme}
import genetic.genotype.syntax._
import genetic.operators.crossover.ParentsOrOffspring
import genetic.operators.mutation.RepetitiveMutation
import genetic.operators.selection.Tournament
import genetic.{OperatorSet, Population, PopulationExtension}
import genetic.engines.streaming._
import genetic.engines.sync.{SequentialGA, ParallelGA, SynchronousGA}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor}

object Compare extends App {

  def measureN[R: Fitness](list: List[(String, () => Population[R])]): Unit = {
    for ((label, popF) <- list) {
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

  val implicits = new MatrixImplicits("http://anjos.mgi.polymtl.ca/qaplib/data.d/had20.dat")

  import implicits._

  val initialPopSize = 5000
  val initialPop = Scheme.make(initialPopSize)
  val operators = OperatorSet(
    Tournament(20),
    ParentsOrOffspring(0.25),
    RepetitiveMutation(0.8, 0.5))

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ex: ExecutionContextExecutor = system.dispatcher


  val iterations = 5

  object GeneticAlgorithm extends SynchronousGA {
    def par: SynchronousGA = ParallelGA

    def evolutionStep[G: Fitness : Join : Modification](population: Population[G], operators: OperatorSet): Population[G] =
      SequentialGA.evolutionStep(population, operators)
  }

  measureN(List(
    ("Basic sync", () => GeneticAlgorithm.evolve(initialPop, operators, iterations)),
    ("Parallel sync", () => GeneticAlgorithm.par.evolve(initialPop, operators, iterations)),
    ("Streaming GA", () => Await.result(GeneticAlgorithm.par.stream.evolve(initialPop, operators, iterations).runWith(Sink.last[Population[Permutation]]), Duration.Inf))
  ))

  system.terminate()
}
