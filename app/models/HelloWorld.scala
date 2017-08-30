package models

import java.util.UUID
import play.api.libs.json._
import reactivemongo.bson.BSONDocument

case class HelloWorld(_id: Option[String], name: String) {
  require(name.nonEmpty, s"name should not empty.")

  def asBSONDocument = BSONDocument(
    "_id" -> this._id.getOrElse(UUID.randomUUID().toString),
    "name" -> this.name
  )
}

object HelloWorld {
  //for validate
  implicit val format = Json.format[HelloWorld]

  implicit object HelloWorldIdentity extends Identity[HelloWorld, String] {
    override def of(entity: HelloWorld): Option[String] = entity._id

    override def set(entity: HelloWorld, id: String): HelloWorld = entity.copy(_id = Some(id))

    override def clear(entity: HelloWorld): HelloWorld = entity.copy(_id = None)

    override def next: String = UUID.randomUUID().toString
  }


}
