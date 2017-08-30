package utils

import org.json4s.FieldSerializer._
import org.json4s.{Extraction, FieldSerializer, Formats, NoTypeHints}
import org.json4s.jackson.{JsonMethods, Serialization}

trait JsonProcessor {
  val serializer = FieldSerializer[Throwable](ignore("stackTrace") orElse ignore("cause"))

  implicit val formats = Serialization.formats(NoTypeHints) + serializer

  def writePretty[A <: Any](a: A)(implicit formats: Formats): String = JsonMethods.mapper.writeValueAsString(Extraction.decompose(a)(formats).camelizeKeys)

}
