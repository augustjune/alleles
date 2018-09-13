import genetic.RRandom


object Main extends App {
  val seed  = util.Random.nextInt
  for (i <- 1 to 10) {
    RRandom.setSeed(seed)
    println(RRandom.nextInt())
  }
}
