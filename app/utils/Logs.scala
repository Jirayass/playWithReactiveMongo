package utils

import akka.event.slf4j.Logger

trait Logs {
  val log = Logger(this.getClass.getName)
}
