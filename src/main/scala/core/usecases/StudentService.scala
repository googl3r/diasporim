package core.usecases

import java.util.UUID

import cats.MonadError
import cats.effect.Sync
import cats.implicits._
import core.UUIDGenerator
import core.domain._

final class StudentService[F[_]: UUIDGenerator](studentRepository: StudentRepository[F])(implicit me: MonadError[F, Throwable]) {
  def create(studentToCreate: CreateStudent): F[StudentId] = {
    val student = for {
      uuid <- UUIDGenerator[F].make
    } yield Student.createStudent(uuid.toString, studentToCreate.name, studentToCreate.email)

    student.flatMap {
      case Right(value) => studentRepository.findByEmail(value.email)
        .flatMap {
          case Some(_) => me.raiseError(EmailInUse(value.email))
          case None => studentRepository.create(value)
        }
      case Left(error) => me.raiseError(error)
    }
  }

}
object StudentService {
  def make[F[_]: Sync](studentRepository: StudentRepository[F]): F[StudentService[F]] =
    Sync[F].delay(
      new StudentService[F](studentRepository)
    )
}
