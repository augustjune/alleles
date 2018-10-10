package genetic.engines.sync

import genetic.GenotypeImplicits
import org.scalacheck.Gen


object SequentialGAProps extends SynchronousGAProps("Sequential genetic algorithm") {
  def implGen: Gen[SynchronousGA] = Gen.const(SequentialGA)

  type G = Int

  val implicits: GenotypeImplicits[Int] = GenotypeImplicits[Int]
}
