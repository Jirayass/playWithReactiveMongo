package config

import akka.actor.ActorSystem
import akka.dispatch.MessageDispatcher
import com.google.inject.{Inject, Provider}
import com.google.inject.name.Names
import utils.Logs
import com.typesafe.config.{Config, ConfigFactory}
import play.api.{Configuration, Environment}
import play.api.inject.{Binding, Module}

class ConfigModule extends Module {
  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = {
    Seq(
      bind[Config].to(ConfigFactory.load()).eagerly(),
      bind[MessageDispatcher].qualifiedWith(Names.named("http-execution")).toProvider[MessageDispatcherProvider].eagerly(),
      bind[MessageDispatcher].qualifiedWith(Names.named("future-execution")).toProvider[FutureDispatcherProvider].eagerly()
    )
  }
}
class MessageDispatcherProvider @Inject()(system: ActorSystem) extends Provider[MessageDispatcher] {
  override def get() = system.dispatchers.lookup("http-execution")
}

class FutureDispatcherProvider @Inject()(system: ActorSystem) extends Provider[MessageDispatcher] with Logs {
  lazy val futureExecution = system.dispatchers.lookup("future-execution")

  override def get(): MessageDispatcher = {
    log.info("Future dispatcher provider execute get method")
    futureExecution
  }
}