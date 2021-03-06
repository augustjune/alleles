package alleles.examples.funParamTuning

import alleles.environment.{Epic, GeneticAlgorithm}
import alleles.genotype.syntax._
import alleles.genotype.{Fitness, Join, Scheme, Variation}
import alleles.stages.{CrossoverStrategy, MutationStrategy, Selection}
import alleles.toolset.RRandom
import alleles.{Epoch, Population, PopulationExtension}
import cats.effect.IO

import scala.language.postfixOps
import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._

object FunTuningRun extends App {

  object Genotype {
    implicit val join: Join[Fun] = Join.symmetric {
      case (Fun(args1), Fun(args2)) => Fun(args1.zipAll(args2, 0.0, 0.0).map { case (a, b) => (a + b) / 2 })
    }

    implicit val scheme: Scheme[Fun] = Scheme.fromOne(Fun(Vector.fill(4)(0)))

    def round(x: Double, dec: Int): Double = {
      val prec = math.pow(10, dec)
      (x * prec).toInt / prec
    }

    implicit val variation: Variation[Fun] = {
      case Fun(args) =>
        val idx = RRandom.nextInt(args.size)
        Fun(args.updated(idx, if (RRandom.nextBoolean) args(idx) + round(RRandom.nextGaussian(), 2) else round(args(idx), 2)))
    }

    def calcFitness(values: Map[Double, Double]): Fitness[Fun] = (fun: Fun) => {
      values.foldLeft(0.0) { case (sum, (x, y)) => sum + math.abs(fun(x) - y) }
    }
  }

  import Genotype._

  implicit val cs = IO.contextShift(ExecutionContext.global)
  implicit val timer = IO.timer(ExecutionContext.global)

  val secretFun = Fun(Vector(5, -2, 1, 4))

  def createValues(fun: Fun, n: Int): Map[Double, Double] = (for (_ <- 1 to n) yield {
    val x = RRandom.nextDouble * 10
    x -> fun(x)
  }).toMap

  val inputValues = createValues(secretFun, 1000)
  implicit val fitness = calcFitness(inputValues)
  val operators = Epoch(Selection.tournament(10), CrossoverStrategy.parentsOrOffspring(0.25), MutationStrategy.repetitiveMutation(0.7, 0.4))

  val population: Population[Fun] =
    GeneticAlgorithm[Fun].evolve(Epic(100, operators)).interruptAfter[IO](10 seconds).compile.lastOrError.unsafeRunSync()

  val best: Fun = population.best

  println(s"Best cost f: " + best.fitness)
  println(best)
  inputValues.foreach { case (x, y) => println(s"f($x) = ${best(x)}| expected: $y | false: ${math.abs(y - best(x)) / y * 100}%") }
}
