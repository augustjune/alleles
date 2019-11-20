package alleles.bench

import alleles._
import alleles.environment.{Epic, GeneticAlgorithm}
import alleles.examples.qap.{Permutation, PermutationOps}
import alleles.genotype.Scheme
import alleles.stages.{CrossoverStrategy, MutationStrategy, Selection}

import scala.language.postfixOps

object Compare extends App {

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
    .map(n => s"Fixed thread pool ($n)" -> ga)
    .map { case (label, setting) => println(s"Running $label"); label -> benchmark.run(setting.evolve(options).take(iterations)) }

  println()
  measurements.foreach {
    case (label, (time, handle)) => println(s"$label result: $time")
  }
  executorServicePool.shutdown()
}
