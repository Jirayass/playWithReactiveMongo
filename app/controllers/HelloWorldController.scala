package controllers


import akka.actor.ActorSystem
import com.google.inject.Inject
import models.HelloWorld
import service.HelloWorldService
import utils.BSON.BSONDocumentConvert
import play.api.libs.json.Json
import play.api.mvc.Action

class HelloWorldController @Inject()(system: ActorSystem, service: HelloWorldService) extends BaseController(system) with BSONDocumentConvert {

  def create() = Action.async(parse.json) { implicit request =>
    validateAndThen[HelloWorld] {
      entity =>
        service.create(entity) map {
          case Right(id) => created(Map("id" -> id))
          case Left(error) => badRequest(error)
        }
    }
  }

  def get(id: String) = Action.async {
    service.findById(id) map (_.fold(
      NotFound(s"Entity #$id not found")
    )(entity =>
      Ok(Json.toJson(entity))))
  }

  def update(id: String) = Action.async(parse.json) { implicit request =>
    validateAndThen[HelloWorld] {
      entity =>
        service.update(id, entity).map {
          case Right(_id) => ok(Map("id" -> _id))
          case Left(err) => badRequest(err)
        }
    }
  }

  def delete(id: String) = Action.async {
    service.deleteById(id).map {
      case Right(_id) => NoContent
      case Left(err) => badRequest(err)
    }
  }

  def search(name: String, cursor: Int, limit: Int) = Action.async {
    service.search($and("name" -> name), cursor, limit) map { item =>
      ok(Map("response" -> item))
    }
  }
}
