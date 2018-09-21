package genetic.engines.async

import akka.stream.ActorMaterializer

import scala.concurrent.{ExecutionContext, Future}

object ParallelizableFuture {
  def pure[A](a: A): ParallelizableFuture[A] = ParallelizableFuture(_ => _ => _ => Future.successful(a))
}

case class ParallelizableFuture[A](innerF: ActorMaterializer => ExecutionContext => Int => Future[A]) {
  def run(parallelism: Int)(implicit mat: ActorMaterializer, exContext: ExecutionContext): Future[A] =
    innerF(mat)(exContext)(parallelism)

  def flatMap[B](f: A => ParallelizableFuture[B]): ParallelizableFuture[B] =
    ParallelizableFuture(mat => ex => i => innerF(mat)(ex)(i).flatMap(a => f(a).innerF(mat)(ex)(i))(ex))

  def map[B](f: A => B): ParallelizableFuture[B] = //ParallelizableFuture(i => (mat, ex) => fn(i)(mat, ex).map(f)(ex))
    flatMap(a => ParallelizableFuture.pure(f(a)))
}
