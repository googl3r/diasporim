import io.circe.Decoder
import io.circe.generic.semiauto._
package object conf {
  implicit val serverConfigDecoder:    Decoder[ServerConfig]              = deriveDecoder
  implicit val connectionDecoder:      Decoder[DatabaseConnectionsConfig] = deriveDecoder
  implicit val databaseConfigDecoder:  Decoder[DatabaseConfig]            = deriveDecoder
  implicit val diasporimConfigDecoder: Decoder[DiasporimConfig]           = deriveDecoder
}
