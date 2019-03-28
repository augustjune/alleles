package alleles

import alleles.environment.Epic

package object bench {

  case class EvolutionPreferences[A](options: Epic[A], iterations: Int)

}
