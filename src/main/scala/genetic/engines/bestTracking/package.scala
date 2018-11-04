package genetic.engines

import genetic.Population

package object bestTracking {
  type PopulationWithBest[G] = (Population[G], (G, Double))
}
