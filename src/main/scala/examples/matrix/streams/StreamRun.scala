package examples.matrix.streams

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import examples.matrix.{MatrixImplicits, Permutation}
import genetic.genotype.Fitness
import genetic.operators.mixing.ClassicCrossover
import genetic.operators.mutation.GenotypeMutation
import genetic.operators.selection.Tournament
import genetic.{AlgoSettings, Population, PopulationExtension, RRandom}

import scala.concurrent.Future

object StreamRun extends App {
  implicit val system = ActorSystem("allele")
  implicit val mat = ActorMaterializer()
  implicit val exContext = system.dispatcher

  val implicits = new MatrixImplicits("http://anjos.mgi.polymtl.ca/qaplib/data.d/had20.dat")

  import implicits._

  val tournament = Tournament(10)
  val crossover = ClassicCrossover(0.25)
  val mutation = GenotypeMutation(0.7)


  val initPopSize = 100
  val settings = AlgoSettings(initPopSize, tournament, crossover, mutation)
  val initPop: Population[Permutation] = settings.initialPopulation

  val parallelism = 4

  def adhocTournament(pop: Seq[Permutation]) = RRandom.shuffle(pop).take(20).minBy(Fitness(_))

  def adhocCrossover(x1: Permutation, x2: Permutation) = x1.crossover(x2)

  def adhocMutation(p: Permutation) = p.mutate

  def iterate(n: Int, population: Seq[Permutation]): Future[Seq[Permutation]] = {
    val size = population.size
    def loop(i: Int, acc: Future[Seq[Permutation]]): Future[Seq[Permutation]] =
      if (i >= n) acc
      else acc.flatMap(x => stage(x, size))

    loop(0, Future(population))
  }

  def stage(population: Seq[Permutation], size: Int) = {
    Source(1 to size)
      .mapAsyncUnordered(parallelism)(_ => Future(adhocTournament(population)))
      .async
      .grouped(2)
      .mapAsyncUnordered(parallelism)(seq => Future(List(adhocCrossover(seq(0), seq(1)), adhocCrossover(seq(1), seq(0)))))
      .mapConcat(identity)
      .mapAsyncUnordered(parallelism)(x => Future(adhocMutation(x)))
      .runWith(Sink.seq)
  }

}
