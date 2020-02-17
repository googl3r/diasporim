package core

import java.util.UUID

import cats.ApplicativeError
import cats.data.{NonEmptyList, Validated}
import cats.implicits._

/*trait StudentValidator[F[_]] {
  def validate(studentToCreate: CreateStudentCommand): F[Student]
}
object StudentValidator {
  implicit val studentValidator = validator[Validated[NonEmptyList[StudentPropertyValidationError], ?],
    NonEmptyList[StudentPropertyValidationError]](NonEmptyList(_, Nil))
  def apply[F[_]](implicit sv: StudentValidator[F]): StudentValidator[F] = sv

  def validator[F[_], E](validationError: StudentPropertyValidationError => E)(implicit A: ApplicativeError[F, E])
  : StudentValidator[F] = new StudentValidator[F] {

    def validateStudentName(name: Name): F[Name] =
      if(name.value.isEmpty)
        A.raiseError(validationError(NameInvalidError("student name is invalid")))
    else
        name.pure[F]

    def validateStudentEmail(email: Email): F[Email] =
      if(email.value.contains("@"))
        email.pure[F]
    else
        A.raiseError(validationError(EmailInvalidError("student email is invalid")))

    override def validate(studentToCreate: CreateStudentCommand): F[Student] =
      (validateStudentName(studentToCreate.name), validateStudentEmail(studentToCreate.email))
        .mapN((_,_) => Student(StudentId(UUID.randomUUID().toString), studentToCreate.name, studentToCreate.email))
  }
  def validate[F[_]: StudentValidator, E](studentToCreate: CreateStudentCommand): F[Student] =
    StudentValidator[F].validate(studentToCreate)
}
*/