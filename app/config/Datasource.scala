package config

import akka.actor.ActorSystem
import com.google.inject.{Inject, Singleton}
import com.typesafe.config.ConfigFactory
import reactivemongo.api.{DefaultDB, MongoConnectionOptions, MongoDriver}

import scala.concurrent.Future

@Singleton
class Datasource @Inject()(system: ActorSystem) {

  implicit val _system = system
  lazy val config = ConfigFactory.load()
  lazy val driver = MongoDriver()

  lazy val db: Future[DefaultDB] = {
    import scala.collection.JavaConversions._
    import scala.concurrent.ExecutionContext.Implicits.global
    val connection = driver.connection(
      nodes = config.getStringList("mongodb.servers"),
      options = MongoConnectionOptions(nbChannelsPerNode = 20)
    )
    connection.database(config.getString("mongodb.db"))
  }
}
