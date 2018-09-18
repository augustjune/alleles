package genetic.genotype

private[genotype] class CachedFitness[G](inner: Fitness[G]) extends Fitness[G] {
  private var cache: Map[G, Int] = Map()

  def value(g: G): Int = cache.get(g) match {
    case Some(value) => value
    case None =>
      val value = inner.value(g)
      cache += g -> value
      value
  }

  override def cached: CachedFitness[G] = this
}

