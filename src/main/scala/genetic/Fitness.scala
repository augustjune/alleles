package genetic

object Fitness {
  def apply[A](a: A)(implicit f: Fitness[A]): Int = f.value(a)
}

trait Fitness[A] {
  def value(a: A): Int
}
