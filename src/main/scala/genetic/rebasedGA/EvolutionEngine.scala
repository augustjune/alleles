package genetic.rebasedGA

import akka.NotUsed
import akka.stream.ActorMaterializer
import akka.stream.impl.SeqStage
import akka.stream.scaladsl.{Sink, Source}
import cats.{Apply, Semigroupal}
import genetic._
import genetic.collections.IterablePair
import genetic.genotype._

import scala.collection.parallel.immutable.ParVector
import scala.concurrent.{ExecutionContext, Future}

trait EvolutionEngine {
  def evolve[G: Fitness : Join : Modification](initial: Population[G], operators: OperatorSet): Source[Population[G], NotUsed] =
    Source.repeat(()).scan(initial) {
      case (prev, _) => evolutionStep(evalFitnesses(prev), operators)
    }

  def evalFitnesses[G: Fitness](population: Population[G]): Population[(G, Double)]

  def evolutionStep[G: Join : Modification](scoredPop: Population[(G, Double)],
                                            operators: OperatorSet): Population[G]

  def withBest = new BestTrackingEvolutionEngine(this)
}

abstract class AsyncEvolutionEngine(inner: EvolutionEngine)(implicit executionContext: ExecutionContext) {

  def evolve[G: AsyncFitness : Join : Modification](initial: Population[G], operators: OperatorSet): Source[Population[G], NotUsed] = {
    Source.repeat(()).scanAsync(initial) {
      case (prev, _) => Future.traverse(prev) { g =>
        for (a <- Future.successful(g); b <- AsyncFitness(g)) yield (a, b)
      }.map(inner.evolutionStep(_, operators))
    }
  }
}

object SeqEvolutionEngine extends EvolutionEngine {
  def par: ParallelEvolutionEngine.type = ParallelEvolutionEngine

  def evalFitnesses[G: Fitness](population: Population[G]): Population[(G, Double)] =
    population.map(g => g -> Fitness(g))

  def evolutionStep[G: Join : Modification](scoredPop: Population[(G, Double)],
                                            operators: OperatorSet): Population[G] =
    operators.mutation.generation(
      operators.crossover.generation(
        operators.selection.generation(scoredPop)
      )
    )
}

object ParallelEvolutionEngine extends EvolutionEngine {
  def evalFitnesses[G: Fitness](population: Population[G]): Population[(G, Double)] =
    population.par.map(g => g -> Fitness(g)).seq

  def evolutionStep[G: Join : Modification](scoredPop: Population[(G, Double)],
                                            operators: OperatorSet): Population[G] = operators match {
    case OperatorSet(selection, crossover, mutation) =>
      ParVector.fill(scoredPop.size / 2)(())
        .map(_ => selection.single(scoredPop))
        .flatMap(crossover.single(_))
        .map(mutation.single(_))
        .seq
  }
}

class BestTrackingEvolutionEngine(inner: EvolutionEngine) {
  def evolve[G: Fitness : Join : Modification](initial: Population[G],
                                               operators: OperatorSet): Source[PopulationWithBest[G], NotUsed] =
    Source.repeat(()).scan((initial, (initial.head, Double.MaxValue))) {
      case ((prev, prevBest), _) =>
        val withFitnesses = inner.evalFitnesses(prev)
        (inner.evolutionStep(withFitnesses, operators), (prevBest +: withFitnesses).minBy(_._2))
    }
}


