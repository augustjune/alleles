package genetic.engines

import genetic.Population

package object bestTracking {
  type PopulationWithBest[A] = (Population[A], Rated[A])
}
