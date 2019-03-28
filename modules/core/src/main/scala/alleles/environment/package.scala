package alleles

import akka.NotUsed
import akka.stream.scaladsl.Source

package object environment {
  type EvolutionFlow[A] = Source[A, NotUsed]
}
