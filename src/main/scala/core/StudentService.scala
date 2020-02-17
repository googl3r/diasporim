package core

import cats.MonadError
import cats.data.Validated.{Invalid, Valid}
import cats.effect.Sync
import cats.implicits._

final class StudentService[F[_]](studentRepository: StudentRepository[F])(implicit me: MonadError[F, Throwable]) {
  def create(studentToCreate: CreateStudentCommand): F[StudentId] = Student.fromCreateCommand(studentToCreate) match {
    case Valid(student) => studentRepository.findByEmail(student.email)
      .flatMap {
        case Some(_) => me.raiseError(EmailInUse(student.email))
        case None => studentRepository.create(student)
    }
    case Invalid(e) => me.raiseError(StudentInvalidError(e.toList))
  }

}
object StudentService {
  def make[F[_]: Sync](studentRepository: StudentRepository[F]): F[StudentService[F]] =
    Sync[F].delay(
      new StudentService[F](studentRepository)
    )
}
