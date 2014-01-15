package controllers

import play.api.mvc._
import scala.concurrent.Future

object Application extends Controller {

	def index =
		Action.async {
			Future.successful(Ok("Hello world!"))
		}
}
