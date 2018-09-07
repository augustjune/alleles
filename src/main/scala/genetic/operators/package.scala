package genetic

package object operators {
  sealed trait GeneticOperator extends (Population => Population)

  trait Selection extends GeneticOperator
  trait Mixing extends GeneticOperator
  trait Mutation extends GeneticOperator

  object Same extends Selection with Mixing with Mutation {
    def apply(pop: Population): Population = pop
  }
}
