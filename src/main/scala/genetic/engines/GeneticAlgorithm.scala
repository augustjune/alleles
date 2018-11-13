package genetic.engines

import genetic.engines.parallel.{ParallelEvolutionStrategy, ParallelFitnessEvaluator}
import genetic.engines.parallel.configurable.{ConfigurableParEvolutionStrategy, ConfigurableParFitnessEvaluator}
import genetic.engines.sequential.{SeqEvolutionStrategy, SeqFitnessEvaluator}

import scala.collection.parallel.TaskSupport

object GeneticAlgorithm extends SeqFitnessEvaluator with SeqEvolutionStrategy {
  def par: EvolutionEngine = new ParallelFitnessEvaluator with ParallelEvolutionStrategy

  def par(taskSupport: TaskSupport): EvolutionEngine = new ConfigurableParFitnessEvaluator with ConfigurableParEvolutionStrategy {
    val taskSupport: TaskSupport = taskSupport
  }

  def parFitness: EvolutionEngine = new ParallelFitnessEvaluator with SeqEvolutionStrategy
}

