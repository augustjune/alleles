package genetic

import akka.NotUsed
import akka.stream.scaladsl.Source

package object engines {
  type Rated[A] = (A, Double)

  type EvolutionFlow[A] = Source[A, NotUsed]
}
