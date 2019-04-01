package alleles.bench

import java.util.concurrent.Executors

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import alleles._
import alleles.bench.Measuring.Measured
import alleles.environment.{Epic, GeneticAlgorithm, Setting}
import alleles.examples.qap.{Permutation, PermutationOps}
import alleles.genotype.Scheme
import alleles.genotype.syntax._
import alleles.stages.{CrossoverStrategy, MutationStrategy}
import alleles.stages.selection.Tournament

import scala.collection.parallel.ExecutionContextTaskSupport
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps

object Compare extends App {

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  val implicits = new PermutationOps("http://anjos.mgi.polymtl.ca/qaplib/data.d/had20.dat")

  import implicits._

  val initialPopSize = 10000
  val initialPop = Scheme.make(initialPopSize)
  val operators = Epoch(
    Tournament(10),
    CrossoverStrategy.parentsOrOffspring(0.25),
    MutationStrategy.repetitiveMutation(0.8, 0.5))

  val options = Epic(initialPop, operators)
  val iterations = 5

  val comparison = new LongRunningComparison[Permutation, Unit] {
    def candidates: List[(String, Setting)] = List(
      "Basic sync" -> GeneticAlgorithm,
      "Fully parallel" -> GeneticAlgorithm.par,
      "Parallel #2" -> GeneticAlgorithm.par(new ExecutionContextTaskSupport(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(2)))),
      "Parallel #4" -> GeneticAlgorithm.par(new ExecutionContextTaskSupport(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(4)))),
      "Parallel #8" -> GeneticAlgorithm.par(new ExecutionContextTaskSupport(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(8)))),
      "Parallel #16" -> GeneticAlgorithm.par(new ExecutionContextTaskSupport(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(16)))),
      "Parallel #32" -> GeneticAlgorithm.par(new ExecutionContextTaskSupport(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(32))))
    )

    val preferences: EvolutionPreferences[Permutation] = EvolutionPreferences(options, iterations)

    val resultMapper: (String, Measured[Population[Permutation]]) => Unit = {
      case (label, measured) => measured.run match {
        case (time, pop) =>
          println(f"$label%-30s : Time: $time with size: ${pop.size} and best candidate: ${pop.best.fitness}")
      }
    }

    override val restTime: FiniteDuration = 3 seconds
  }

  comparison.run
  /*val vec = Vector.fill(1000)(RRandom.nextInt)
  val m = new Measuring {}
   val mapF: Int => Int = x => {
    val start = System.currentTimeMillis()
    while(start + 5 > System.currentTimeMillis()) {}
    x
  }

  GeneticAlgorithm.evolve()
  println(s"Mapping function: ${m.measure(mapF(0)).written}")
  println(s"Sequential approach ${m.measure(vec.map(mapF)).written}")
  println(s"Parallel approach ${m.measure(vec.par.map(mapF).seq).written}")*/
  system.terminate()
}
