package genetic

object RandomChange {
  def apply[A](a: A)(implicit m: RandomChange[A]): A = m.modify(a)
}

trait RandomChange[A] {
  def modify(a: A): A
}
