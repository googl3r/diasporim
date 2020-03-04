package infrastructure.rest.student

import cats.effect.Sync
import cats.implicits._
import core.domain.EmailInUse
import core.usecases.{CreateStudent, StudentService}
import io.circe.Json
import io.circe.generic.auto._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

class StudentRoutes[F[_]: Sync](studentService: StudentService[F]) extends Http4sDsl[F]{

  private val studentRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root =>
      req.decode[StudentRequest]{
        studentRequest => StudentRequest.validator.validate(studentRequest) match {
          case Some(errors) => BadRequest(errors)
          case None => studentService.create(CreateStudent(studentRequest.name, studentRequest.email))
              .flatMap(stdId => Created(Json.obj(("id", Json.fromString(stdId.value)))))
              .handleErrorWith {
                case EmailInUse(e) => Conflict(e.value)
              }
        }
      }
  }
  val routes: HttpRoutes[F] = Router(
    "/students" -> studentRoutes
  )

}
