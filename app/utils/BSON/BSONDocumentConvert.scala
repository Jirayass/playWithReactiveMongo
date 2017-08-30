package utils.BSON

import reactivemongo.bson.{BSONDocument, BSONValue}

trait BSONDocumentConvert {

  sealed trait BSONValueWrapper extends Any

  private case class BSONValueWrapperImpl(field: BSONValue) extends BSONValueWrapper

  implicit def toJsValueWrapper[T](field: T)(implicit w: Writes[T]): BSONValueWrapper = BSONValueWrapperImpl(w.writes(field))

  private def fieldsToBSONDocument(fields: (String, BSONValueWrapper)*): BSONDocument = BSONDocument(fields.map(f => (f._1, f._2.asInstanceOf[BSONValueWrapperImpl].field)))

  def $and(fields: (String, BSONValueWrapper)*): BSONDocument = fieldsToBSONDocument(fields: _*)

  def $one(field: (String, BSONValueWrapper)): BSONDocument = fieldsToBSONDocument(field)

  def $set(fields: (String, BSONValueWrapper)*): BSONDocument = fieldsToBSONDocument(fields: _*)

}
