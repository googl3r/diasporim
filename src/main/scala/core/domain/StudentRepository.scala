package core.domain

trait StudentRepository[F[_]] {
  def findByEmail(email: Email): F[Option[Student]]
  def create(student: Student): F[StudentId]

}
