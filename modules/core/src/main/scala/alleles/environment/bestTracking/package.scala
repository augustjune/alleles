package alleles.environment

import alleles.Population
import alleles.genotype.Fitness.Rated

package object bestTracking {
  type PopulationWithBest[A] = (Population[A], Rated[A])
}
