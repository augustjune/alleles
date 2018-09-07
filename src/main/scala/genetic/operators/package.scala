package genetic

package object operators {
  sealed trait GeneticOperator extends (Population => Population)

  trait Selection extends GeneticOperator
  trait Mixing extends GeneticOperator
  trait Mutation extends GeneticOperator
}
