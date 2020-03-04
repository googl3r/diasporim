package core

import java.util.UUID

import cats.effect.Sync

trait UUIDGenerator[F[_]] {
  def make: F[UUID]
}
object UUIDGenerator {
  def apply[F[_]](implicit uuidGenerator: UUIDGenerator[F]): UUIDGenerator[F] = uuidGenerator

  implicit def syncUUIDGenerator[F[_]: Sync]: UUIDGenerator[F] = new UUIDGenerator[F] {
    override def make: F[UUID] = Sync[F].delay(UUID.randomUUID())
  }
}
