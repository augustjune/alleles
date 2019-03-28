package alleles.genotype

import scala.collection.concurrent.TrieMap

/**
  * Concurrent thread-safe non-blocking cached version of Fitness
  */
class CachedFitness[A] private[genotype](inner: Fitness[A]) extends Fitness[A] {
  private var cache: TrieMap[A, Double] = TrieMap.empty

  def value(a: A): Double = cache.get(a) match {
    case Some(fitness) => fitness
    case None =>
      val fitness = inner.value(a)
      cache += a -> fitness
      fitness
  }

  override def cached: CachedFitness[A] = this
}
