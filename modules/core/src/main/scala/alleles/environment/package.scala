package alleles

import fs2.{Stream, Pure}

package object environment {
  type EvolutionFlow[A] = Stream[Pure, A]
}
