package service

import utils.BSON.BSONDocumentConvert
import reactivemongo.api.{QueryOpts, ReadPreference}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteConcern
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter}

import scala.concurrent.{ExecutionContext, Future}


trait MongoCURDService[E] extends BSONDocumentConvert {
  implicit def writer: BSONDocumentWriter[E]

  implicit def reader: BSONDocumentReader[E]

  def collection(implicit ec: ExecutionContext): Future[BSONCollection]

  def insert(doc: BSONDocument)(implicit ec: ExecutionContext) = collection.flatMap(_.insert(doc))

  def findById(id: String)(implicit ec: ExecutionContext): Future[Option[E]] = collection.flatMap(_.find($one("_id" -> id)).one[E])

  def deleteById(id: String)(implicit ec: ExecutionContext): Future[Either[String, String]] = collection.flatMap(_.remove($one("_id" -> id))) map {
    case le if le.ok && le.n > 0 => Right(id)
    case le => Left(le.writeConcernError.map(_.errmsg).getOrElse("not found."))
  }

  def search(s: BSONDocument, cursor: Int, limit: Int)(implicit ec: ExecutionContext) = collection
    .flatMap(_.find(s)
      .options(QueryOpts(skipN = (cursor - 1) * limit, batchSizeN = limit))
      .cursor[E](readPreference = ReadPreference.primaryPreferred)
      .collect[List](limit))

  def update(s: BSONDocument, update: BSONDocument, wc: WriteConcern = WriteConcern.Acknowledged)(implicit ec: ExecutionContext) = collection
    .flatMap(_.update(selector = s, update = update, writeConcern = wc, upsert = false, multi = true))

}
