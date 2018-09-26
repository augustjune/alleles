package genetic.operators.crossover

import cats.Semigroup
import cats.syntax.semigroup._
import genetic.collections.IterablePair
import org.scalacheck.{Arbitrary, Gen, Properties}
import org.scalacheck.Prop._

object ParentsOrBreedSpecification extends Properties("ParentsOrBreed") {
  /*property("startsWith") = forAll { (a: String, b: String) =>
    (a + b).startsWith(a)
  }

  property("cancatenate") = forAll { (a: String, b: String) =>
    (a + b).length > a.length && (a + b).length > b.length
  }

  property("substring") = forAll { (a: String, b: String, c: String) =>
    (a +  b + c).substring(a.length, a.length + b.length) == b
  }*/

  def containSameElements[A](i1: Iterable[A], i2: Iterable[A]): Boolean =
    i1.size == i2.size && i1.toSet == i2.toSet

  type G = String
  implicit val combiner: Semigroup[G] = (a, b) => a + b

  property("100% parents") = forAll{ (p1: G, p2: G) =>
    ParentsOrBreed(1).single(p1, p2) == IterablePair(p1, p2)
  }

  property("0% parents") = forAll { (p1: G, p2: G) =>
    containSameElements(ParentsOrBreed(0).single(p1, p2), List(p2 |+| p1, p1 |+| p2))
  }

  implicit val parentsOrBreedGen = Arbitrary(Arbitrary.arbDouble.arbitrary.map(ParentsOrBreed(_)))

  property("Both parents or both breed") = forAll { (p: ParentsOrBreed, p1: G, p2: G) =>
    val result = p.single(p1, p2)
    containSameElements(result, List(p1, p2)) || containSameElements(result, List(p1 |+| p2, p2 |+| p1))
  }
}
