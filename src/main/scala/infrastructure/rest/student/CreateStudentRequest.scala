package infrastructure.rest.student

import infrastructure.rest.validation.{EmailValidation, NotEmpty, Validator}

case class CreateStudentRequest(name: String, email: String)

object CreateStudentRequest {
  import cats.implicits._
  implicit val validator: Validator[CreateStudentRequest] = (studentRequest: CreateStudentRequest) => {
    NotEmpty.validate(studentRequest.name, "name") |+|
      EmailValidation.validate(studentRequest.email, "email")
  }
}

