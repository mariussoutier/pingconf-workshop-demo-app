package services

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.libs.ws.WS
import play.api.libs.oauth._
import play.api.libs.json._

import models._
import common._

object Twitter extends CurrentApp {

	lazy val config = play.api.Play.configuration

	val consumerKey = ConsumerKey(
		config.getString("twitter.consumer.key").getOrElse("???"),
		config.getString("twitter.consumer.secret").getOrElse("???"))

	val accessToken = RequestToken(
		config.getString("twitter.access.key").getOrElse("???"),
		config.getString("twitter.access.secret").getOrElse("???"))

	def searchForKeyword(keyword: String): Future[Seq[Tweet]] =
		WS
			.url("https://api.twitter.com/1.1/search/tweets.json")
			.withQueryString("q" -> s"#$keyword")
			.sign(OAuthCalculator(consumerKey, accessToken))
			.get()
			.map { response =>
				val statuses = (response.json \ "statuses").as[Seq[JsObject]]
				val tweets = statuses map { status =>
					val id = (status \ "id").as[String]
					val name = (status \ "user" \ "name").as[String]
					val text = (status \ "text").as[String]
					val hashtags: Seq[String] = (status \ "entities" \ "hashtags" \\ "text") map (_.toString)
					Tweet(id, text = text, author = name, hashtags = hashtags)
				}

				tweets
			}
}
