package infrastructure

import cats.effect._
import conf.{DatabaseConfig, DiasporimConfig}
import doobie.util.ExecutionContexts
import infrastructure.modules.{HttpApi, Repositories, Services}
import org.http4s.server.blaze.BlazeServerBuilder
import doobie.hikari.HikariTransactor
import io.circe.config.parser


object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = transactor.use { tx =>
    for {
      conf          <- confFile
      repositories  <- Repositories.make[IO](tx)
      services      <- Services.make[IO](repositories)
      api           <- HttpApi.make[IO](services)
      _             <- BlazeServerBuilder[IO]
                      .bindHttp(conf.server.port, conf.server.host)
                      .withHttpApp(api.httpApp)
                      .serve
                      .compile
                      .drain
    } yield ExitCode.Success

  }
  val confFile: IO[DiasporimConfig] = parser.decodePathF[IO, DiasporimConfig]("diasporim")

  val transactor: Resource[IO, HikariTransactor[IO]] = for {
    conf <- Resource.liftF(confFile)
    threadPool    <- ExecutionContexts.fixedThreadPool[IO](32)
    cachedThread  <- ExecutionContexts.cachedThreadPool[IO]
    tx            <- DatabaseConfig.databaseTransactor[IO](conf.db, threadPool, Blocker.liftExecutionContext(cachedThread))
  } yield tx
}
