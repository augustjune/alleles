package genetic

/**
  * Fitness function for instances of type `A`, which computes
  * how close a given design solution is to achieving the set aims
  */
trait Fitness[-A] {
  def value(a: A): Int
}

object Fitness {
  /**
    * Applies trait function from the implicit scope;
    * allows to use trait as context bounds
    */
  def apply[A](a: A)(implicit f: Fitness[A]): Int = f.value(a)
}
