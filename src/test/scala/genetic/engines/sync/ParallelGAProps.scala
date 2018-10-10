package genetic.engines.sync

import genetic.GenotypeImplicits
import org.scalacheck.Gen

object ParallelGAProps extends SynchronousGAProps("Parallel genetic algorithm") {
  def implGen: Gen[SynchronousGA] = Gen.const(ParallelGA)

  type G = String

  val implicits: GenotypeImplicits[G] = GenotypeImplicits[String]
}
