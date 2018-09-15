package examples.matrix

import genetic.engines.CounterGA
import genetic.operators.mixing.ClassicCrossover
import genetic.operators.mutation.ComplexMutation
import genetic.operators.selection.Tournament
import genetic.{AlgoSettings, RRandom}
import org.scalameter._

object Benchmark extends App {

  val implicits = new MatrixImplicits("/home/jura/Projects/jslinkin/allele/src/main/resources/matrix_14.txt")
  import implicits._

  RRandom.setSeed(1563046509)
  val settings: AlgoSettings[Permutation] = AlgoSettings[Permutation](100, Tournament(10), ClassicCrossover(0.3), ComplexMutation(0.7, 0.3))

  val time = config(
    Key.exec.benchRuns -> 5,

    Key.verbose -> true
  ) withWarmer {
    new Warmer.Default
  } withMeasurer {
    new Measurer.IgnoringGC
  } measure {
    RRandom.setSeed(1563046509)
    CounterGA.evolve(settings, 100)
  }
  println(s"Total time: $time")
}
