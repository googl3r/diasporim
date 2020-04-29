package integrations.repositories

import cats.effect.IO
import core.domain.{Email, Student}
import doobie.implicits._
import infrastructure.repositories.DoobieStudentRepository
import integrations.testTransactor
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class DoobieStudentRepositorySpec extends AnyWordSpec with Matchers with BeforeAndAfterEach {
    def transactor: doobie.Transactor[IO] = testTransactor
  val studentToCreate = Student.createStudent("123", "pouulo", "pouulodia@gmail.com")

  override protected def beforeEach(): Unit = {
    super.beforeEach()
    sql"""delete from STUDENT""".update.run.transact(transactor).unsafeRunSync()
  }


  "DoobieStudentRepository " should {
    "create a student in the database" in {
      val studentRepository = new DoobieStudentRepository[IO](transactor)
      val studentId = studentRepository.create(studentToCreate.toOption.get).unsafeRunSync()

      studentId.value should equal("123")
    }
    "find a student by email" in {
      val email = "pouulodia@gmail.com"
      val studentRepository = new DoobieStudentRepository[IO](transactor)
      val result = studentRepository.create(studentToCreate.toOption.get)
        .flatMap(_ => studentRepository.findByEmail(Email.fromString(email).toOption.get)).unsafeRunSync()

      result should equal(studentToCreate.toOption)

    }
  }

  override protected def afterEach(): Unit = {
    super.afterEach()
    sql"""delete from STUDENT""".update.run.transact(transactor).unsafeRunSync()
  }
}
