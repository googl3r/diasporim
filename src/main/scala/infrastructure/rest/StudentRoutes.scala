package infrastructure.rest

import cats.effect.Sync
import core.{CreateStudentCommand, EmailInUse, StudentInvalidError, StudentService}
import org.http4s.HttpRoutes
import io.circe.generic.auto._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl
import cats.implicits._
import org.http4s.server.Router

class StudentRoutes[F[_]: Sync](studentService: StudentService[F]) extends Http4sDsl[F]{

  private val studentRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root =>
    val action = for {
      request <- req.as[CreateStudentCommand]
      result <- studentService.create(request)
    } yield result

      action.flatMap(stdId => Created(stdId.value))
      .handleErrorWith {
        case EmailInUse(e) => Conflict(e.value)
        case StudentInvalidError(err) =>BadRequest(err.foldLeft("")((a,b) => a +b))
      }
  }
  val routes: HttpRoutes[F] = Router(
    "/students" -> studentRoutes
  )

}
