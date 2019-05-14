package alleles.bench

import java.util.concurrent.ExecutorService

class ExecutorServicePool {
  private var pool: List[ExecutorService] = Nil

  def register(service: => ExecutorService): ExecutorService = {
    val ec = service
    pool = ec :: pool
    ec
  }

  def shutdown(): Unit = {
    pool.foreach(_.shutdown())
    pool = Nil
  }
}
