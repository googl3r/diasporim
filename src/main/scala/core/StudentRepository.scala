package core

trait StudentRepository[F[_]] {
  def findByEmail(email: Email): F[Option[Student]]
  def create(student: Student): F[StudentId]

}
