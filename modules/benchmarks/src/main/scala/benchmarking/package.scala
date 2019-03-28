import genetic.engines.Epic

package object benchmarking {
  case class EvolutionPreferences[A](options: Epic[A], iterations: Int)
}
