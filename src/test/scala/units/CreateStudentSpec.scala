package units

import cats.effect.IO
import core.{CreateStudentCommand, Email, EmailInUse, EmailInvalidError, Name, NameInvalidError, Student, StudentId, StudentInvalidError, StudentRepository, StudentService}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CreateStudentSpec extends AnyWordSpec with Matchers with BeforeAndAfterAll{
  "Student creation" should {
    val name = "pouulo"
    val email = "pouulodia@gmail.com"
    " not create Invalid student" should {
      val invalidName = ""
      val invalidEmail = "pouulodiagmail.com"
      val studentRepository = new StudentTestRepository
      val studentService = new StudentService(studentRepository)

      "shouldn't create a student when name is invalid" in {
        val createStudentCommand = CreateStudentCommand(invalidName, email)

        studentService.create(createStudentCommand)
          .attempt
          .map {
            case Left(StudentInvalidError(list)) => assert(list.size == 1 &&
              list.head == NameInvalidError("student name is invalid"))
            case _ => fail("impossible to create a student with an invalid name")
          }.unsafeRunSync()
      }

      "shouldn't create a student with an invalid email" in {
        val createStudentCommand = CreateStudentCommand(name, invalidEmail)

        studentService.create(createStudentCommand)
          .attempt
          .map {
            case Left(StudentInvalidError(list)) => assert(list.size == 1 &&
              list.head == EmailInvalidError("student email is invalid"))
            case _ => fail("impossible to create a student with an invalid email")
          }.unsafeRunSync()

      }
    }

    "shouldn't create a student with an email in use" in {
      val studentToCreate = CreateStudentCommand(name, email)
      val studentRepository: StudentRepository[IO] = new StudentTestRepository {
        override def findByEmail(email: Email): IO[Option[Student]] = IO.pure(Student.createStudent("123", name, email.value).toOption)
      }
      val studentService = new StudentService(studentRepository)

      studentService.create(studentToCreate)
        .attempt
        .map {
          case Left(EmailInUse(_)) => assert(true)
          case _ => fail("shouldn't create a student with an email in use")
        }.unsafeRunSync()

    }
    "student should be created" in {
      val studentRepository = new StudentTestRepository {
        override def create(student: Student): IO[StudentId] = IO.pure(StudentId.fromString("123").toOption.get)
      }
      val studentToCreate = CreateStudentCommand(name, email)
      val studentService = new StudentService(studentRepository)

      studentService.create(studentToCreate)
        .map {
          studentId => studentId should equal (StudentId.unsafeFromString("123"))
        }.unsafeRunSync()
    }
  }

  class StudentTestRepository extends StudentRepository[IO] {
    override def findByEmail(email: Email): IO[Option[Student]] = IO.pure(None)

    override def create(student: Student): IO[StudentId] = IO.pure(student.id)
  }

}
