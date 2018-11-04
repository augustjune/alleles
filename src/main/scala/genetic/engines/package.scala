package genetic

import genetic.engines.parallel.{ConfigurableParallelEvolutionEngine, ParallelEvolutionEngine}

import scala.collection.parallel.TaskSupport

package object engines {

  object GeneticAlgorithm extends SequentialEvolutionEngine {
    def par: EvolutionEngine = new ParallelEvolutionEngine

    def par(taskSupport: TaskSupport): EvolutionEngine = new ConfigurableParallelEvolutionEngine(taskSupport)
  }

}
