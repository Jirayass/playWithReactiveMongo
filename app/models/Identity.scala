package models

trait Identity[E, String] {

  def of(entity: E): Option[String]

  def set(entity: E, id: String): E

  def clear(entity: E): E

  def next: String
}
