package genetic

import genetic.operators._

object AlgoSettings {
  def apply(selection: Selection, operators: Seq[GeneticOperator]) =
    new AlgoSettings(operators.foldLeft(selection: Population => Population)(_ andThen _))

  def apply(selection: Selection = Same, mixing: Mixing = Same, mutation: Mutation = Same) =
    new AlgoSettings(selection andThen mixing andThen mutation)
}

case class AlgoSettings(cycle: Population => Population)
