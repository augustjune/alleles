package genetic.engines

import genetic.engines.parallel.{ParallelEvolutionFlow, ParallelFitnessEvaluator}
import genetic.engines.parallel.configurable.{ConfigurableParEvolutionFlow, ConfigurableParFitnessEvaluator}
import genetic.engines.sequential.{SeqEvolutionFlow, SeqFitnessEvaluator}

import scala.collection.parallel.TaskSupport

object GeneticAlgorithm extends CompositeDriver(SeqFitnessEvaluator, SeqEvolutionFlow) {
  def par: CompositeDriver = new CompositeDriver(ParallelFitnessEvaluator, ParallelEvolutionFlow)

  def par(taskSupport: TaskSupport): CompositeDriver =
    new CompositeDriver(
      new ConfigurableParFitnessEvaluator(taskSupport),
      new ConfigurableParEvolutionFlow(taskSupport))

  def parFitness: CompositeDriver = new CompositeDriver(ParallelFitnessEvaluator, SeqEvolutionFlow)
}

