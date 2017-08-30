package service

import akka.actor.{Actor, ActorRef}
import akka.dispatch.MessageDispatcher
import akka.util.Timeout
import com.google.inject.Inject
import com.google.inject.name.Named
import utils.Logs

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

abstract class BaseActorService extends Actor with Logs {
  @Inject
  @Named("future-execution") implicit var dispatcher: MessageDispatcher = null
  implicit val timeOut: Timeout = 10.seconds

  def sendResponse2Controller(a: ActorRef, r: Future[Option[ServiceResponse]]): Unit = r onComplete {
    case Success(Some(x: ServiceResponse)) =>
      a ! x
    case Success(None) =>
      a ! NoContent204Response
    case Failure(e) =>
      log.error(s"Some unExpect error happens in ${self.path.name} ", e)
      a ! InternalServerErrorResponse
  }

}

trait ServiceMsg

sealed trait ServiceResponse

case class CreatedResponse(body: Option[Any]) extends ServiceResponse

case object DeleteResponse extends ServiceResponse

case object NoContent204Response extends ServiceResponse

case class OKResponse(body: Option[Any]) extends ServiceResponse

case class OkListResponse(response: ResponseBody) extends ServiceResponse

case class BadRequestResponse(response: ResponseBody) extends ServiceResponse

case object InternalServerErrorResponse extends ServiceResponse

sealed trait ResponseBody

case class FailResponseBody(errors: List[ErrorDefine]) extends ResponseBody

sealed trait ErrorDefine

case class FieldError(field: String, reason: String) extends ErrorDefine

case class DescriptionError(reason: String) extends ErrorDefine

case class ActionError(change: Any, reason: String) extends ErrorDefine


