package errorhandler

import akka.actor.ActorSystem
import akka.dispatch.MessageDispatcher
import com.google.inject.{Inject, Provider}
import com.google.inject.name.Named
import org.json4s.MappingException
import play.api.{Configuration, Environment, OptionalSourceMapper}
import play.api.http.DefaultHttpErrorHandler
import play.api.mvc.{RequestHeader, Result, Results}
import play.api.routing.Router

import scala.concurrent.Future

class JsonHttpErrorHandler @Inject()(system: ActorSystem,
                                     env: Environment,
                                     config: Configuration,
                                     sourceMapper: OptionalSourceMapper,
                                     router: Provider[Router]
                                    ) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) with Results {
  @Inject
  @Named("http-execution") implicit var dispatcher: MessageDispatcher = null

  override protected def onBadRequest(request: RequestHeader, message: String): Future[Result] = Future {
    BadRequest(message)
  }

  override protected def onForbidden(request: RequestHeader, message: String): Future[Result] = Future {
    Forbidden(message)
  }

  override protected def onNotFound(request: RequestHeader, message: String): Future[Result] = Future {
    NotFound(message)
  }

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = Future {
    if (exception.isInstanceOf[MappingException]) {
      BadRequest(exception.getMessage.replace("\n", ". "))
    } else {
      InternalServerError("Internal Server Error.")
    }
  }
}

