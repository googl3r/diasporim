package conf

import cats.effect.{Async, Blocker, ContextShift, Resource, Sync}
import doobie.hikari.HikariTransactor
import org.flywaydb.core.Flyway
import cats.syntax.functor._

import scala.concurrent.ExecutionContext
case class DatabaseConnectionsConfig(poolSize: Int)
case class DatabaseConfig(url: String,
                          driver: String,
                          user: String,
                          password: String,
                          connections: DatabaseConnectionsConfig)

object DatabaseConfig {
  def databaseTransactor[F[_]: Async: ContextShift]
  (db: DatabaseConfig, connection: ExecutionContext, blocker: Blocker): Resource[F, HikariTransactor[F]] =
    HikariTransactor.newHikariTransactor[F](db.driver, db.url, db.user, db.password, connection, blocker)

  def initializeDB[F[_]](cfg: DatabaseConfig)(implicit S: Sync[F]): F[Unit] =
    S.delay {
      val fw: Flyway = {
        Flyway.configure().dataSource(cfg.url, cfg.user, cfg.password).load()
      }
      fw.migrate()
    }
    .as(())
}
