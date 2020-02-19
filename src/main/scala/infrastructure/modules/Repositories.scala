package infrastructure.modules

import cats.effect.Sync
import core.StudentRepository
import doobie.util.transactor.Transactor
import infrastructure.repositories.DoobieStudentRepository
import cats.implicits._

object Repositories {
  def make[F[_]: Sync](tx: Transactor[F]): F[Repositories[F]] = for {
    studentRepository <- DoobieStudentRepository.make[F](tx)
  } yield new Repositories[F](studentRepository)

}
final class Repositories[F[_]] private(val studentRepository: StudentRepository[F])
