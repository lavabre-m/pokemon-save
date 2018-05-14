import cats._
import cats.Semigroup
import cats.implicits._

object scala_Ex_Cat{

  Semigroup[Int].combine(1, 2)
  Semigroup[List[Int]].combine(List(1, 2, 3), List(4, 5, 6))
  Semigroup[Option[Int]].combine(Option(1), Option(2))
  Semigroup[Option[Int]].combine(Option(1), None)
  Semigroup[Int ⇒ Int].combine({ (x: Int) ⇒ x + 1 }, { (x: Int) ⇒ x * 10 }).apply(6)


  Option(1).map(_ + 1)
  List(1, 2, 3).map(_ + 1)
  Vector(1, 2, 3).map(_.toString)

  Functor[Option].map(Option("Hello"))(_.length)

  val lenOption: Option[String] => Option[Int] = Functor[Option].lift(_.length)
  lenOption(Some("abcd"))

  val source = List("Cats", "is", "awesome")
  val product = Functor[List].fproduct(source)(_.length).toMap

  product.get("Cats").getOrElse(0)

  val addArity2 = (a: Int, b: Int) ⇒ a + b
  val addArity3 = (a: Int, b: Int, c: Int) ⇒ a + b + c

  val option2 = Option(1) |@| Option(2)
  val option3 = option2 |@| Option.empty[Int]

  option2 map addArity2
  option3 map addArity3
  option2 apWith Some(addArity2)

  option2.tupled

  Monad[List].flatMap(List(1, 2, 3))(x ⇒ List(x, x))

  Monad[List].ifM(List(true, false, true))(List(1, 2), List(3, 4))
  Foldable[List].fold(List("a", "b", "c"))
  Foldable[List].foldMap(List("a", "b", "c"))(_.length)

  Foldable[List].foldK(List(List(1, 2), List(3, 4, 5)))
  Foldable[List].foldK(List(None, Option("two"), Option("three")))


  val listOption = List(None, Some(1), Some(2))

  for {
    opt <- lenOption
  } yield {
    opt match {
      case Some(x) => Some(x + 1)
      case None => None
    }
  }
}