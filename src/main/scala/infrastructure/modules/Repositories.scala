package infrastructure.modules

import cats.Parallel
import cats.effect.{Concurrent, Sync, Timer}
import core.StudentRepository
import doobie.util.transactor.Transactor
import infrastructure.repositories.DoobieStudentRepository
import cats.implicits._

object Repositories {
  def make[F[_]: Concurrent: Parallel: Timer](tx: Transactor[F]): F[Repositories[F]] = for {
    studentRepository <- DoobieStudentRepository.make[F](tx)
  } yield new Repositories[F](studentRepository)

}
final class Repositories[F[_]] private(val studentRepository: StudentRepository[F]){}
