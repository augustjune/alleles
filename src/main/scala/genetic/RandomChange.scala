package genetic

/**
  * Any function which allows to create a new instance of type `A`
  * which differs from the original one in random place
  *
  * Laws:
  *   1. Modified instance does not equal to original one
  *     f(a) != a
  *   2. After a certain number of modification the same input produces different outputs
  *     def f5(a: A) = f(f(f(f(f(a)))))
  *     f5(a) != f5(a)
  */
trait RandomChange[A] {
  def modify(a: A): A
}

object RandomChange {
  /**
    * Applies trait function from the implicit scope;
    * allows to use trait as context bounds
    */
  def apply[A](a: A)(implicit m: RandomChange[A]): A = m.modify(a)
}
