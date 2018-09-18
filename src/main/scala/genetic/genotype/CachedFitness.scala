package genetic.genotype

private[genotype] class CachedFitness[G](inner: Fitness[G]) extends Fitness[G] {
  private var cache: Map[G, Int] = Map()

  def value(g: G): Int = cache.get(g) match {
    case Some(fitness) => fitness
    case None =>
      val fitness = inner.value(g)
      cache += g -> fitness
      fitness
  }

  override def cached: CachedFitness[G] = this
}
