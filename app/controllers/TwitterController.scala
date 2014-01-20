package controllers

import scala.concurrent.ExecutionContext.Implicits.global

import play.api.mvc._

import play.api.libs.ws.WS
import play.api.libs.json._

import models._
import common._

/** Demonstration of how to use the Tweeter actor */
trait TwitterController extends Controller with CurrentApp {

	// we need this for ? (= ask), so Akka knows when to give up waiting for the response
	implicit val timeout = akka.util.Timeout(5000)

	def searchForKeyword(keyword: String) = Action.async {
		import akka.actor._
		import akka.pattern.ask
		import actors.TweetActor
		import actors.TweetActor._

		val tweeter = play.api.libs.concurrent.Akka.system.actorOf(TweetActor.props)

		(tweeter ? SearchForKeyword(keyword)).mapTo[Tweets] map { tweets =>
			tweeter ! PoisonPill
			val tweetsAsJson = tweets.tweets.map { tweet =>
				import Tweet._
				Json.toJson(tweet)
			}
			Ok(Json.obj("res" -> tweetsAsJson))
		} recover {
			case _: Throwable => {
				tweeter ! PoisonPill
				Ok(Json.obj("res" -> "Twitter error"))
			}
		}
	}

}

object TwitterController extends TwitterController
