package service


import com.google.inject.{Inject, Singleton}
import models.{HelloWorld, Identity}
import config.Datasource
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocumentReader, BSONDocumentWriter, Macros}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HelloWorldService @Inject()(datasource: Datasource) extends MongoCURDService[HelloWorld] {
  override def collection(implicit ec: ExecutionContext): Future[BSONCollection] = datasource.db.map(_.collection("helloWorld"))

  override implicit def writer: BSONDocumentWriter[HelloWorld] = Macros.writer[HelloWorld]

  override implicit def reader: BSONDocumentReader[HelloWorld] = Macros.reader[HelloWorld]

  def create(entity: HelloWorld)(implicit identity: Identity[HelloWorld, String], ec: ExecutionContext): Future[Either[String, String]] = {
    val id = identity.next
    insert(identity.set(entity, id).asBSONDocument) map {
      case le if le.ok => Right(id)
      case le => Left(le.writeConcernError.map(_.errmsg).getOrElse(""))
    }
  }

  def update(id: String, entity: HelloWorld)(implicit ec: ExecutionContext): Future[Either[String, String]] = {
    collection.flatMap(_.update($one("_id" -> id), entity)) map {
      case le if le.ok && le.n > 0 => Right(id)
      case le => Left(le.writeConcernError.map(_.errmsg).getOrElse(""))
    }
  }
}

