package infrastructure.repositories

import cats.effect.{Bracket, Sync}
import core._
import doobie._
import doobie.implicits._
import doobie.util.transactor.Transactor
import infrastructure.entities.StudentEntity

class DoobieStudentRepository[F[_]: Bracket[?[_], Throwable]](val tx: Transactor[F])
extends StudentRepository[F]{

  override def findByEmail(email: Email): F[Option[Student]] =
    sqlFindByEmail(email).map(studentEntity => StudentEntity.toStudent(studentEntity)).option.transact(tx)

  override def create(student: Student): F[StudentId] =
    sqlCreateStudent(StudentEntity.fromStudent(student))
    .map(id => StudentId.fromString(id).toOption.get)
      .transact(tx)


  def sqlCreateStudent(student: StudentEntity): ConnectionIO[String] = for {
    id        <- insert(student).update.withUniqueGeneratedKeys[Int]("ID")
    studentId <- selectById(id).query[String].unique
  } yield studentId


  def sqlFindByEmail(email: Email): Query0[StudentEntity] = sql"""
    SELECT STUDENTID, NAME, EMAIL FROM STUDENT WHERE EMAIL = ${email.value}
    """.query[StudentEntity]


  def selectById(id: Int) = fr"SELECT STUDENTID from STUDENT WHERE ID = $id"
  def insert(student: StudentEntity) = fr"INSERT INTO STUDENT(STUDENTID, NAME, EMAIL) VALUES (${student.studentId}, ${student.name}, ${student.email})"


  /*implicit val studentIdMeta: Meta[StudentId] = Meta[String].timap(id => StudentId.unsafeFromStudentId(id))(studentId => studentId.value)
  implicit val nameMeta: Meta[Name] = Meta[String].timap(name => Name.unsafeName(name))(name => name.value)
  implicit val emailMeta: Meta[Email] = Meta[String].timap(email => Email.unsafeEmail(email))(email => email.value)*/
}
object DoobieStudentRepository {
  def make[F[_]: Sync](tx: Transactor[F]): F[DoobieStudentRepository[F]] =
    Sync[F].delay(
      new DoobieStudentRepository[F](tx)
    )
}
