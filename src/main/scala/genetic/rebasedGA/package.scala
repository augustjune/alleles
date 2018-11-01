package genetic

package object rebasedGA {
  type PopulationWithBest[G] = (Population[G], (G, Double))
}
