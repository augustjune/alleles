package genetic.engines

import genetic.engines.parallel.{ParallelEvolutionStrategy, ParallelFitnessEvaluator}
import genetic.engines.parallel.configurable.{ConfigurableParEvolutionStrategy, ConfigurableParFitnessEvaluator}
import genetic.engines.sequential.{SeqEvolutionStrategy, SeqFitnessEvaluator}

import scala.collection.parallel.TaskSupport

object GeneticAlgorithm extends EvolutionEngine with SeqFitnessEvaluator with SeqEvolutionStrategy {
  def par: EvolutionEngine = new EvolutionEngine with ParallelFitnessEvaluator with ParallelEvolutionStrategy

  def par(taskSupport: TaskSupport): EvolutionEngine =
    new EvolutionEngine with ConfigurableParFitnessEvaluator with ConfigurableParEvolutionStrategy {
      protected val configuration: TaskSupport = taskSupport
    }

  def parFitness: EvolutionEngine = new EvolutionEngine with ParallelFitnessEvaluator with SeqEvolutionStrategy
}

