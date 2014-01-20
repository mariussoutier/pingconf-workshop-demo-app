package controllers

import play.api.mvc._
import scala.concurrent.Future

import redis.RedisClient

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import common._

object Application extends Controller with CurrentApp {

	def index = Action.async {
		implicit val akkaSystem = play.api.libs.concurrent.Akka.system
		val redis = RedisClient()

		redis ping () map { res =>
			Ok(s"Redis response: $res")
		}
	}

}
