package alleles.bench

import alleles.Population
import alleles.toolset.RRandom
import fs2.{Pure, Stream}

class AmbienceBenchmark[A](seed: Long = RRandom.nextLong()) extends Measuring {
  def run(flow: Stream[Pure, Population[A]]): (Long, List[Population[A]]) = {
    measureMillis(flow.toList).run
  }
}
