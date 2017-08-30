package controllers

import akka.actor.{ActorRef, ActorSystem}
import akka.dispatch.MessageDispatcher
import akka.pattern._
import akka.util.Timeout
import com.google.inject.Inject
import com.google.inject.name.Named
import service._
import utils.{JsonProcessor, Logs}
import play.api.libs.json._
import play.api.mvc.{Controller, Request, Result}

import scala.concurrent.Future
import scala.concurrent.duration._

abstract class BaseController(system: ActorSystem) extends Controller with JsonProcessor with Logs {

  @Inject
  @Named("future-execution") implicit var dispatcher: MessageDispatcher = null
  implicit val timeout = Timeout(30.seconds)


  def askServiceResponse[T <: ActorRef, U <: ServiceMsg](a: T, m: U): Future[Result] = ask(a, m).response

  def validateAndThen[T: Reads](t: T => Future[Result])(implicit request: Request[JsValue]) = {
    try {
      request.body.validate[T].map(t) match {
        case JsSuccess(result, _) =>
          result.recover { case e => BadRequest(e.getMessage) }
        case JsError(err) =>
          Future.successful(BadRequest(Json.toJson(err.map {
            case (path, errors) => Json.obj("path" -> path.toString, "errors" -> JsArray(errors.flatMap(e => e.messages.map(JsString(_)))))
          })))
      }
    } catch {
      case ex: Exception =>
        log.error(ex.getMessage)
        Future.successful(BadRequest(ex.getMessage))
    }
  }

  private implicit class HandleServiceResponse(future: Future[Any]) {
    def response: Future[Result] = {
      future map {
        case CreatedResponse(body) => created(body)
        case DeleteResponse => NoContent
        case OKResponse(body) => ok(body)
        case NoContent204Response => NoContent
        case OkListResponse(body) => ok(body)
        case BadRequestResponse(body) => badRequest(body)
        case InternalServerErrorResponse => InternalServerError
      }
    }
  }

  def created(body: Any) = Created(Json.parse(writePretty(body)))

  def ok(body: Any) = Ok(Json.parse(writePretty(body)))

  def badRequest(body: Any) = BadRequest(Json.parse(writePretty(body)))
}
