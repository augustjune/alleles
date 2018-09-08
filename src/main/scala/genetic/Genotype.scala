package genetic

trait Genotype {
  def fitnessValue: Int

  /**
    * Creates a new permutation with single chromosome mutated
    * @return mutated Permutation
    */
  def mutate: Genotype

  /**
    * Creates a new permutation containing part of the original genome
    * with part of the genes from other permutation, split in random place
    * @param other permutation providing second part of genome
    * @return permutation containing genes from both genomes
    */
  def crossover(other: Genotype): Genotype
}
