import genetic.engines.EvolutionOptions

package object benchmarking {
  case class EvolutionPreferences[G](options: EvolutionOptions[G], iterations: Int)
}
