package genetic.genotype

import scala.collection.concurrent.TrieMap

/**
  * Concurrent thread-safe non-blocking cached version of Fitness
  */
class CachedFitness[G] private[genotype](inner: Fitness[G]) extends Fitness[G] {
  private var cache: TrieMap[G, Double] = TrieMap.empty

  def value(g: G): Double = cache.get(g) match {
    case Some(fitness) => fitness
    case None =>
      val fitness = inner.value(g)
      cache += g -> fitness
      fitness
  }

  override def cached: CachedFitness[G] = this
}
