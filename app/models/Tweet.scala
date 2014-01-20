package models

case class Tweet(
	id: String,
	text: String,
	hashtags: Seq[String],
	author: String)

object Tweet {
	import play.api.libs.json._

	implicit val TweetWrites = Json.writes[Tweet]
}
