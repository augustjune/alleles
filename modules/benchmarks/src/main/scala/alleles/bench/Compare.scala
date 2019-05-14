package alleles.bench

import java.util.concurrent.Executors

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import alleles._
import alleles.environment.{Epic, GeneticAlgorithm}
import alleles.examples.qap.{Permutation, PermutationOps}
import alleles.genotype.Scheme
import alleles.stages.{CrossoverStrategy, MutationStrategy, Selection}

import scala.collection.parallel.ExecutionContextTaskSupport
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}
import scala.language.postfixOps

object Compare extends App {

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  val implicits = new PermutationOps("http://anjos.mgi.polymtl.ca/qaplib/data.d/had20.dat")

  import implicits._

  val initialPopSize = 10000
  val initialPop = Scheme.make(initialPopSize)
  val operators = Epoch(
    Selection.tournament(10),
    CrossoverStrategy.parentsOrOffspring(0.25),
    MutationStrategy.repetitiveMutation(0.8, 0.5))

  val options = Epic(initialPop, operators)
  val iterations = 10

  val executorServicePool = new ExecutorServicePool()
  val benchmark = new AmbienceBenchmark[Permutation]()
  val ga = GeneticAlgorithm[Permutation]

  val measurements = List(2, 4, 8, 16, 32)
    .map(n => s"Fixed thread pool ($n)" -> ga.par(new ExecutionContextTaskSupport(ExecutionContext.fromExecutor(executorServicePool.register(Executors.newFixedThreadPool(n))))))
    .map { case (label, setting) => println(s"Running $label"); label -> benchmark.run(setting.evolve(options).take(iterations)) }

  println()
  measurements.foreach {
    case (label, (time, handle)) => println(s"$label result: $time")
  }


  Await.ready(system.terminate(), Duration.Inf)
  executorServicePool.shutdown()
}
