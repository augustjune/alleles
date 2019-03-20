package genetic.engines

import genetic.engines.parallel.{ParallelEvolution, ParallelFitnessEvaluator}
import genetic.engines.parallel.configurable.{ConfigurableParEvolution, ConfigurableParFitnessEvaluator}
import genetic.engines.sequential.{SeqEvolution, SeqFitnessEvaluator}

import scala.collection.parallel.TaskSupport

object GeneticAlgorithm extends CompositeDriver(SeqFitnessEvaluator, SeqEvolution) {
  def par: CompositeDriver = new CompositeDriver(ParallelFitnessEvaluator, ParallelEvolution)

  def par(taskSupport: TaskSupport): CompositeDriver =
    new CompositeDriver(
      new ConfigurableParFitnessEvaluator(taskSupport),
      new ConfigurableParEvolution(taskSupport))

  def parFitness: CompositeDriver = new CompositeDriver(ParallelFitnessEvaluator, SeqEvolution)
}

