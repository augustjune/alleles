package examples.funParamTuning

import cats.kernel.Semigroup
import genetic.RRandom
import genetic.genotype.{Fitness, Modification, Scheme}

case class Fun(arguments: Vector[Double]) extends (Double => Double) {
  def apply(x: Double): Double =
    arguments.zipWithIndex.foldLeft(0.0) { case (sum, (arg: Double, pos: Int)) => sum + arg * math.pow(x, pos) }

  override def toString(): String = arguments.zipWithIndex.map { case (a, pos) => s"($a * x^$pos)" }.mkString("f(x) = ", " + ", "")
}

object Fun {

  object Genotype {
    implicit val sem: Semigroup[Fun] = (x: Fun, y: Fun) => (x, y) match {
      case (Fun(args1), Fun(args2)) => Fun(args1.zipAll(args2, 0, 0).map { case (a: Double, b: Double) => (a + b) / 2 })
    }

    implicit val scheme: Scheme[Fun] = Scheme.fromOne(Fun(Vector.fill(2)(0)))

    def round(x: Double, dec: Int): Double = {
      val prec = math.pow(10, dec)
      (x * prec).toInt / prec
    }

    implicit val modification: Modification[Fun] = {
      case Fun(args) =>
        val idx = RRandom.nextInt(args.size)
        Fun(args.updated(idx, args(idx) + round(RRandom.nextGaussian(), 2)))
    }

    def calcFitness(values: Map[Double, Double]): Fitness[Fun] = (fun: Fun) => {
      values.foldLeft(0.0) { case (sum, (x, y)) => sum + math.abs(fun(x) - y) }
    }
  }

}
