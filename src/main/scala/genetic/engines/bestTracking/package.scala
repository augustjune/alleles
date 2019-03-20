package genetic.engines

import genetic.Population
import genetic.genotype.Fitness.Rated

package object bestTracking {
  type PopulationWithBest[A] = (Population[A], Rated[A])
}
