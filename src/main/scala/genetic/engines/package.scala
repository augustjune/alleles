package genetic

import akka.NotUsed
import akka.stream.scaladsl.Source

package object engines {
  type Rated[G] = (G, Double)

  type EvolutionFlow[G] = Source[G, NotUsed]
}
