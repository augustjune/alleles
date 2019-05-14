package alleles.bench

import akka.Done
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import alleles.Population
import alleles.environment.EvolutionFlow
import alleles.toolset.RRandom

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class AmbienceBenchmark[A](seed: Long = RRandom.nextLong())
                          (implicit mat: ActorMaterializer) extends Measuring {
  def run(flow: EvolutionFlow[Population[A]]): (Long, Future[Done]) = {
    measureMillis(Await.ready(flow.runWith(Sink.ignore), Duration.Inf)).run
  }
}
