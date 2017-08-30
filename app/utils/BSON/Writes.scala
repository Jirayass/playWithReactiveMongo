package utils.BSON

import reactivemongo.bson._

import scala.annotation.implicitNotFound

@implicitNotFound(
  "No Json serializer found for type ${A}. Try to implement an implicit Writes or Format for this type."
)
trait Writes[-A] {
  def writes(o: A): BSONValue
}

object Writes extends DefaultWrites {
  def apply[A](f: A => BSONValue): Writes[A] = new Writes[A] {
    override def writes(o: A): BSONValue = f(o)
  }
}

trait DefaultWrites {

  implicit object IntWrites extends Writes[Int] {
    override def writes(o: Int): BSONValue = BSONInteger(o)
  }

  implicit object LongWrites extends Writes[Long] {
    override def writes(o: Long): BSONValue = BSONLong(o)
  }

  implicit object DoubleWrites extends Writes[Double] {
    override def writes(o: Double): BSONValue = BSONDouble(o)
  }

  implicit object BooleanWrites extends Writes[Boolean] {
    override def writes(o: Boolean): BSONValue = BSONBoolean(o)
  }

  implicit object StringWrites extends Writes[String] {
    override def writes(o: String): BSONValue = BSONString(o)
  }

}
