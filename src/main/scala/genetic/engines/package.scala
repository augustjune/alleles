package genetic

import genetic.engines.parallel.{ConfigurableParallelEvolutionEngine, FullyParallelEvolutionEngine}

import scala.collection.parallel.TaskSupport

package object engines {

  object GeneticAlgorithm extends SequentialEvolutionEngine {
    def par: EvolutionEngine = new FullyParallelEvolutionEngine

    def par(taskSupport: TaskSupport): EvolutionEngine = new ConfigurableParallelEvolutionEngine(taskSupport)
  }

}
