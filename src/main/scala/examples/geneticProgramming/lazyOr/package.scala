package examples.geneticProgramming

import genetic.toolset.RRandom

import scala.language.implicitConversions

package object lazyOr {
  implicit def anyToLazyOr[A](a: => A): LazyOr[A] = NilOr >|| a

  implicit def lazyOrCall[A](or: LazyOr[A]): A = or.one()

  trait LazyOr[+T] {
    def one(): T

    def >||[U >: T](next: => U): LazyOr[U] = lor(next)

    def lor[U >: T](next: => U): LazyOr[U]

    private[lazyOr] def call(idx: Int): T
  }

  private[lazyOr] class LazyOrCons[+T](element: => T, tail: LazyOr[T], size: Int) extends LazyOr[T] {
    def one(): T = call(RRandom.nextInt(size))

    def lor[U >: T](next: => U): LazyOrCons[U] = new LazyOrCons[U](next, this, size + 1)

    def call(idx: Int): T =
      if (idx == 0) element
      else tail.call(idx - 1)
  }

  private[lazyOr] object NilOr extends LazyOr[Nothing] {
    def one(): Nothing = call(0)

    def call(idx: Int): Nothing = throw new RuntimeException("There is nothing to call boi")

    def lor[A](next: => A): LazyOr[A] = new LazyOrCons[A](next, this, 1)
  }

}
