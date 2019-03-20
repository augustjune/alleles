import genetic.engines.EvolutionOptions

package object benchmarking {
  case class EvolutionPreferences[A](options: EvolutionOptions[A], iterations: Int)
}
