package examples.funParamTuning

import genetic.genotype.{Fitness, Join, Variation, Scheme}
import genetic.toolset.RRandom

case class Fun(arguments: Vector[Double]) extends (Double => Double) {
  def apply(x: Double): Double =
    arguments.zipWithIndex.foldLeft(0.0) { case (sum, (arg: Double, pos: Int)) => sum + arg * math.pow(x, pos) }

  override def toString(): String = arguments.zipWithIndex.map { case (a, pos) => s"($a * x^$pos)" }.mkString("f(x) = ", " + ", "")
}

object Fun {


}
