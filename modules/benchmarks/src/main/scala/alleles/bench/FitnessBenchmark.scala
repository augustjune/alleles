package alleles.bench

import alleles.genotype.Fitness
import org.scalameter.{Bench, Gen}

abstract class FitnessBenchmark[A] extends Bench.LocalTime {
  def individuals: Gen[A]

  def fitness: Fitness[A]

  performance of "Fitness evaluation" in {
    measure method "single individual" in {
      using(individuals) in { ind =>
        fitness.value(ind)
      }
    }
  }
}
