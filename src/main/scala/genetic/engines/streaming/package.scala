package genetic.engines

import akka.stream.ActorMaterializer
import genetic.engines.sync.SynchronousGA

import scala.concurrent.ExecutionContext

package object streaming {
  implicit class StreamingSupport(private val syncGA: SynchronousGA) extends AnyVal {
    def stream(implicit mat: ActorMaterializer, ex: ExecutionContext): StreamingGA = new StreamingGA(syncGA)
  }
}
