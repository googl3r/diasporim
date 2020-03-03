package core.domain

import java.util.UUID


import scala.util.control.NoStackTrace

sealed abstract case class StudentId(value: String)
object StudentId {
  def fromString(value: String): Either[StudentPropertyValidationError, StudentId] =
    Either.cond(!value.isEmpty, new StudentId(value) {}, InvalidStudentId("id cannot be empty"))

  def unsafeFromString(value: String): StudentId = fromString(value).toOption.get
}

sealed abstract case class Name(value: String)
object Name {
  def fromString(value: String): Either[StudentPropertyValidationError, Name] =
    Either.cond(!value.isEmpty, new Name(value) {}, NameInvalidError("student name is invalid"))

}

sealed abstract case class Email(value: String)
object Email {
  def fromString(value: String): Either[StudentPropertyValidationError, Email] =
    Either.cond(value.contains("@"), new Email(value) {}, EmailInvalidError("student email is invalid"))
}

sealed abstract case class Student(id: StudentId, name: Name, email: Email)
object Student {
  type StudentValidation[A] = Either[StudentPropertyValidationError, A]
  def createStudent(id: String, name: String, email: String): StudentValidation[Student] = {
    for {
      id    <- StudentId.fromString(id)
      name  <- Name.fromString(name)
      email <- Email.fromString(email)
    } yield new Student(id, name, email) {}
  }

}

sealed trait StudentPropertyValidationError extends NoStackTrace
case class NameInvalidError(message: String) extends StudentPropertyValidationError
case class EmailInvalidError(message: String) extends StudentPropertyValidationError
case class InvalidStudentId(message: String) extends StudentPropertyValidationError

case class EmailInUse(email: Email) extends NoStackTrace
