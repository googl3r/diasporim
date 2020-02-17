package core

import java.util.UUID

import cats.data.Validated.{Invalid, Valid}
import cats.data.ValidatedNel
import cats.implicits._

import scala.util.control.NoStackTrace

sealed abstract case class StudentId(value: String)
object StudentId {
  def fromString(value: String): ValidatedNel[StudentPropertyValidationError, StudentId] =
    if(value.isEmpty) Invalid(InvalidStudentId("student id is invalid")).toValidatedNel
    else Valid(new StudentId(value) {})

  def unsafeFromString(value: String): StudentId = fromString(value).toOption.get
}

sealed abstract case class Name(value: String)
object Name {
  def fromString(value: String): ValidatedNel[StudentPropertyValidationError, Name] =
    if(value.isEmpty) Invalid(NameInvalidError("student name is invalid")).toValidatedNel
    else Valid(new Name(value) {})

}

sealed abstract case class Email(value: String)
object Email {
  def fromString(value: String): ValidatedNel[StudentPropertyValidationError, Email] =
    if(value.isEmpty || !value.contains("@")) Invalid(EmailInvalidError("student email is invalid")).toValidatedNel
  else Valid(new Email(value) {})
}

sealed abstract case class Student(id: StudentId, name: Name, email: Email)
object Student {
  type StudentValidation[A] = ValidatedNel[StudentPropertyValidationError, A]
  def createStudent(id: String, name: String, email: String): StudentValidation[Student] =
    (StudentId.fromString(id),
      Name.fromString(name),
      Email.fromString(email)).mapN((studentId, stdName, stdEmail) => new Student(studentId,stdName, stdEmail) {})

  def fromCreateCommand(createStudentCommand: CreateStudentCommand): StudentValidation[Student] =
    createStudent(UUID.randomUUID().toString, createStudentCommand.name, createStudentCommand.email)
}

sealed trait StudentPropertyValidationError extends NoStackTrace
case class NameInvalidError(message: String) extends StudentPropertyValidationError
case class EmailInvalidError(message: String) extends StudentPropertyValidationError
case class InvalidStudentId(message: String) extends StudentPropertyValidationError
case class StudentInvalidError(errors: List[StudentPropertyValidationError]) extends NoStackTrace

case class EmailInUse(email: Email) extends NoStackTrace
