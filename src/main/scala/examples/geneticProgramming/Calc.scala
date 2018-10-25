package examples.geneticProgramming

trait Calc[T] {
  def eval(t: T): Double
}

object Calc {
  def tree(variables: Map[String, Double]): Calc[GPTree] = new Calc[GPTree] {
    def eval(t: GPTree): Double = t match {
      case Variable(name) => variables.get(name.toLowerCase()) match {
        case Some(value) => value
        case None => throw new RuntimeException(s"Unknown variable :'$name'")
      }
      case Value(v) => v
      case Plus(a, b) => eval(a) + eval(b)
      case Minus(a, b) => eval(a) - eval(b)
      case Multiply(a, b) => eval(a) * eval(b)
      case Divide(a, b) => eval(a) / eval(b)
      case Sin(a) => math.sin(eval(a))
      case Cos(a) => math.cos(eval(a))
    }
  }
}
