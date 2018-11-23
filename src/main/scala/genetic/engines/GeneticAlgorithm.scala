package genetic.engines

import genetic.engines.parallel.{ParallelEvolutionStrategy, ParallelFitnessEvaluator}
import genetic.engines.parallel.configurable.{ConfigurableParEvolutionStrategy, ConfigurableParFitnessEvaluator}
import genetic.engines.sequential.{SeqEvolutionStrategy, SeqFitnessEvaluator}

import scala.collection.parallel.TaskSupport

object GeneticAlgorithm extends EvolutionEngine(SeqFitnessEvaluator, SeqEvolutionStrategy) {
  def par: EvolutionEngine = new EvolutionEngine(ParallelFitnessEvaluator, ParallelEvolutionStrategy)

  def par(taskSupport: TaskSupport): EvolutionEngine =
    new EvolutionEngine(
      new ConfigurableParFitnessEvaluator(taskSupport),
      new ConfigurableParEvolutionStrategy(taskSupport))

  def parFitness: EvolutionEngine = new EvolutionEngine(ParallelFitnessEvaluator, SeqEvolutionStrategy)
}

