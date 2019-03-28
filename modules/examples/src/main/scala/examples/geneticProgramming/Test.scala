package examples.geneticProgramming

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import genetic.engines.{Epic, GeneticAlgorithm}
import genetic.genotype.{Fitness, Join, Scheme, Variation}
import genetic.operators.crossover.ParentsOrOffspring
import genetic.operators.mutation.RepetitiveMutation
import genetic.operators.selection.Tournament
import genetic.toolset.RRandom
import genetic.{Epoch, Population}

import scala.concurrent.Await
import scala.concurrent.duration._

object Test extends App {

  implicit def doubleToValue(d: Double): Value = Value(d)

  implicit def intToValue(i: Int): Value = Value(i.toDouble)

  implicit def stringToVariable(s: String): Variable = Variable(s)

  def prettyTree(t: GPTree): String = t match {
    case Variable(name) => name
    case Value(v) => v.toString
    case Sin(a) => s"sin(${prettyTree(a)})"
    case Cos(a) => s"cos(${prettyTree(a)})"
    case Plus(a, b) => s"(${prettyTree(a)} + ${prettyTree(b)})"
    case Minus(a, b) => s"(${prettyTree(a)} - ${prettyTree(b)})"
    case Multiply(a, b) => s"(${prettyTree(a)} * ${prettyTree(b)})"
    case Divide(a, b) => s"(${prettyTree(a)} / ${prettyTree(b)})"
  }

  class GPTreeOps(generator: TreeGen, goal: GPTree) {
    implicit val join: Join[GPTree] = Join.pair(_.cross(_))

    implicit val variation: Variation[GPTree] = new TreeVariation(generator)

    implicit val scheme: Scheme[GPTree] = new TreeScheme(generator)
  }


  def arbFitness[A](goal: A, n: Int, varRange: Double)(implicit aCalc: Calc[A]): Fitness[GPTree] = (g: GPTree) => {
    aCalc.eval(goal)
  }

  def calcs(n: Int, varRanges: Map[String, Double]): List[Calc[GPTree]] =
    (1 to n).map(_ => Calc.tree(varRanges.mapValues(RRandom.inRange))).toList

  def treeFitness(goal: GPTree, calcs: List[Calc[GPTree]]): Fitness[GPTree] = g =>
    calcs.foldLeft(0.0) { case (sumError, c) => sumError + math.abs(c.eval(goal) - c.eval(g)) } / calcs.size

  val variables = List("x")
  val generator = new TreeGen(variables)

  val goal: GPTree = "x"
    //Plus(Sin("x"), Divide("x", 2))

  implicit val fintess = treeFitness(goal, calcs(100, variables.map(_ -> 100.0).toMap))

  val ops = new GPTreeOps(generator, goal)
  import ops._

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  val operators = new Epoch(Tournament(20), new ParentsOrOffspring(0.5), new RepetitiveMutation(0.4, 0.2))

  val lastPop: Population[GPTree] = Await.result(
    GeneticAlgorithm.par.evolve(Epic(100, operators)).take(1000).runWith(Sink.last),
    Duration.Inf)

  import genetic.PopulationExtension
  import genetic.genotype.syntax._

  val best = lastPop.best
  println("Target: " + prettyTree(goal))
  println("Evolved: " + prettyTree(best))
  println("Fitness: " + best.fitness)

  system.terminate()

}

