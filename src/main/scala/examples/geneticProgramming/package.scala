package examples

package object geneticProgramming {

  implicit class MapTuple[T1, T2](private val tuple: (T1, T2)) extends AnyVal {
    def mapFirst[V](f: T1 => V): (V, T2) = tuple match {
      case (x, y) => (f(x), y)
    }

    def mapSecond[V](f: T2 => V): (T1, V) = tuple match {
      case (x, y) => (x, f(y))
    }

    def reverse: (T2, T1) = tuple match {
      case (x, y) => (y, x)
    }

    def map[V](f: (T1, T2) => V): V = f(tuple._1, tuple._2)
  }
}
