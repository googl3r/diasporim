package conf
final case class ServerConfig(host: String, port: Int)
case class DiasporimConfig(db: DatabaseConfig, server: ServerConfig)
