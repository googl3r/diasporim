package infrastructure.rest

case class StudentRequest(name: String, email: String)

object StudentRequest {
  import cats.implicits._
  implicit val validator: Validator[StudentRequest] = (studentRequest: StudentRequest) => {
    NotEmpty.validate(studentRequest.name, "name") |+|
      EmailValidation.validate(studentRequest.email, "email")
  }
}

