package genetic.engines

import akka.stream.ActorMaterializer
import cats.data.Kleisli

import scala.concurrent.{ExecutionContext, Future}

package object async {
  type ParArgs = (ActorMaterializer, ExecutionContext, Int)

  type ParFutureK[A] = Kleisli[Future, ParArgs, A]

  object KleisliPF {
    private def curry[A](f: ActorMaterializer => ExecutionContext => Int => Future[A]): ParArgs => Future[A] = {
      case (mat, ex, i) => f(mat)(ex)(i)
    }

    def apply[A](f: ActorMaterializer => ExecutionContext => Int => Future[A]): ParFutureK[A] =
      Kleisli(curry(f))
  }

  implicit class ParFuture[A](private val kleisli: ParFutureK[A]) extends AnyVal {
    def run(par: Int)(implicit mat: ActorMaterializer, ex: ExecutionContext) = kleisli.run(mat, ex, par)
  }
}
