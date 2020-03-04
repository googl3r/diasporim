package infrastructure.rest.validation

import cats.data.NonEmptyList
import cats.implicits._

trait Validator[T] {
 def validate(target: T): Option[NonEmptyList[FieldError]]
}
trait FieldValidator[T] {
  def validate(field: T, fieldName: String): Option[NonEmptyList[FieldError]]
}
case object NotEmpty extends FieldValidator[String] {
  override def validate(field: String, fieldName: String): Option[NonEmptyList[FieldError]] =
    if (field == null || field.isEmpty) NonEmptyList.of(FieldError(fieldName, "must not be empty")).some else None
}
case object EmailValidation extends FieldValidator[String] {
  override def validate(field: String, fieldName: String): Option[NonEmptyList[FieldError]] =
    if(field == null || !field.contains("@")) NonEmptyList.of(FieldError(fieldName, "must contains an @")).some else None
}
