import cats.effect.{Async, ContextShift, Effect, IO}
import conf.{DatabaseConfig, DiasporimConfig}
import doobie.util.transactor.Transactor
import io.circe.config.parser
import conf._
import cats.implicits._

import scala.concurrent.ExecutionContext

package object integrations {
  def getTransactor[F[_]: Async: ContextShift](conf: DatabaseConfig): Transactor[F] =
    Transactor.fromDriverManager[F](
      conf.driver,
      conf.url,
      conf.user,
      conf.password
    )

  def initializeTransactor[F[_]: Effect: Async: ContextShift]: F[Transactor[F]] =
    for {
      diasporimConf <- parser.decodePathF[F, DiasporimConfig]("diasporim")
      _ <- DatabaseConfig.initializeDB(diasporimConf.db)
    } yield getTransactor(diasporimConf.db)

  lazy val testExecutionContext = ExecutionContext.Implicits.global
  implicit lazy val testCs = IO.contextShift(testExecutionContext)
  lazy val testTransactor = initializeTransactor[IO].unsafeRunSync()

}
