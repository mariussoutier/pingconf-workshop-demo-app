package actors

import scala.concurrent.duration._

import akka.actor._

import services.Twitter
import models._

object TweetActor {
	case class SearchForKeyword(keyword: String)
	case class Tweets(tweets: Seq[Tweet])

	def props: Props = Props(classOf[TweetActor])
}

/**
 * Actor that fetches Tweets from Twitter's search API for a given keyword.
 * Responds to the callee every 2 seconds the latest set of Tweets.
 * Send a PoisonPill to make it stop (or invoke it from an Actor, too).
 */
class TweetActor extends Actor with ActorLogging {
	import TweetActor._

	implicit val ec = context.dispatcher

	var running: Cancellable = _

	def receive = waitingForKeywordRequest

	val waitingForKeywordRequest: Receive = {
		case SearchForKeyword(keyword) => {
			val lastSender = sender
			running = context.system.scheduler.schedule(0.millisecond, 2.seconds) {
				searchFor(keyword, lastSender)
			}
			context.become(scanningTweets)
		}
		case _ => log.warning("Unknown message")
	}

	val scanningTweets: Receive = {
		case _ => log.warning("Not accepting any messages while in fetch mode")
	}

	def searchFor(keyword: String, originalSender: ActorRef) = {
		Twitter.searchForKeyword(keyword) map { tweets =>
			originalSender ! Tweets(tweets)
		} onFailure {
			case ex: Throwable => {
				log.warning(s"Tweets could not be retrieved ${ex.getMessage}")
			}
		}
	}

	override def postStop(): Unit = {
		log.info("Shutting down")
		running.cancel()
		super.postStop()
	}

}
