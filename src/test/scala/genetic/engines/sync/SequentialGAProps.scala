package genetic.engines.sync
import org.scalacheck.Gen


object SequentialGAProps extends SynchronousGAProps("SequentialGA") {
  def implGen: Gen[SynchronousGA] = Gen.const(SequentialGA)
}
