import genetic.genotype.{Fitness, Scheme}

object MainTest extends App {
  val fitI: Fitness[Int] = (g: Int) => 2

  val schemeI: Scheme[Int] = Scheme.pure(() => 2)
}
