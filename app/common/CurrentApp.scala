package common

/**
 * Trait that makes the implicit dependency of a running Application explicit.
 * Can easily be overriden in unit tests.
 */
trait CurrentApp {
	implicit val app: play.api.Application = play.api.Play.current
}
