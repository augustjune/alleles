package alleles.bench

import alleles.Population
import alleles.environment.EvolutionFlow
import alleles.toolset.RRandom

class AmbienceBenchmark[A](seed: Long = RRandom.nextLong()) extends Measuring {
  def run(flow: EvolutionFlow[Population[A]]): (Long, List[Population[A]]) = {
    measureMillis(flow.toList).run
  }
}
