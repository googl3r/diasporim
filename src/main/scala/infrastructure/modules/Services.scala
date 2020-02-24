package infrastructure.modules

import cats.{MonadError, Parallel}
import cats.effect.{Concurrent, Sync, Timer}
import core.StudentService

object Services {
  def make[F[_]: Concurrent: Parallel: Timer](repositories: Repositories[F])
  : F[Services[F]] = Sync[F].delay(new Services[F](repositories))
}
final class Services[F[_]] private(val repositories: Repositories[F])(implicit me: MonadError[F, Throwable]) {

  val studentService: StudentService[F] = new StudentService[F](repositories.studentRepository)
}