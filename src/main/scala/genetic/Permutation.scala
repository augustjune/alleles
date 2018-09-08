package genetic

trait Permutation {
  def fitnessValue: Int
  def mutate: Permutation
  def randomCrossover(other: Permutation): Permutation
}
