package examples.funParamTuning

import genetic.engines.sync.ParallelGA
import genetic.genotype.syntax._
import genetic.operators.crossover.ParentsOrBreed
import genetic.operators.mutation.RepetitiveMutation
import genetic.operators.selection.Tournament
import genetic.{OperatorSet, Population, PopulationExtension}

object FunTuningRun extends App {

  import Fun.Genotype._

  val values = Map(0.0 -> 10.0, 1.0 -> 12.0, 2.0 -> 14.0, 5.0 -> 20.0)
  implicit val fitness = calcFitness(values)
  val operators = OperatorSet(Tournament(10), ParentsOrBreed(0.25), RepetitiveMutation(0.7, 0.4))

  val population: Population[Fun] = ParallelGA.createAndEvolve(100, operators, 1000)
  val best: Fun = population.best

  println(s"Best cost f: " + best.fitness)
  println(best)
  values.foreach { case (x, y) => println(s"f($x) = ${best(x)}| expected $y") }
}
