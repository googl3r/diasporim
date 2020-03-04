package infrastructure.modules

import cats.effect.{Concurrent, Sync, Timer}
import infrastructure.rest.student.StudentRoutes
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.server.middleware.{AutoSlash, CORS, RequestLogger, ResponseLogger, Timeout}
import org.http4s.implicits._
import org.http4s.server.Router

import scala.concurrent.duration._

object HttpApi {
  def make[F[_]: Concurrent: Timer](services: Services[F]): F[HttpApi[F]] =
    Sync[F].delay(
      new HttpApi[F](services)
    )

}
final class HttpApi[F[_]: Concurrent: Timer] private(services: Services[F]) {
  private val studentRoutes = new StudentRoutes[F](services.studentService).routes

  private val diasporimRoutes: HttpRoutes[F] = studentRoutes

  private val routes: HttpRoutes[F] = Router(
    "/v1" -> diasporimRoutes
  )
  private val middleware: HttpRoutes[F] => HttpRoutes[F] = {
    {http: HttpRoutes[F] => AutoSlash(http)} andThen
    {http: HttpRoutes[F] => CORS(http, CORS.DefaultCORSConfig)} andThen
    {http: HttpRoutes[F] => Timeout(60.seconds)(http)}
  }
  private val loggers: HttpApp[F] => HttpApp[F] = {
    {http: HttpApp[F] => RequestLogger.httpApp(true, true)(http)} andThen
    {http: HttpApp[F] => ResponseLogger.httpApp(true, true)(http)}
  }
  val httpApp: HttpApp[F] = loggers(middleware(routes).orNotFound)
}
