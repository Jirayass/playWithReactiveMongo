package config

import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport


class ServiceModule extends AbstractModule with AkkaGuiceSupport {
  override def configure(): Unit = {

  }
}
