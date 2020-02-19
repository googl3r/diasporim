package infrastructure.modules

import cats.effect.Sync
import core.StudentService
import cats.implicits._

object Services {
  def make[F[_]: Sync](repositories: Repositories[F])
  : F[Services[F]] = for {
    studentService <- StudentService.make[F](repositories.studentRepository)
  } yield new Services[F](studentService)
}
final class Services[F[_]] private(val studentService: StudentService[F])