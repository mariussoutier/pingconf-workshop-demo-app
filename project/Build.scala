import sbt._
import Keys._
import play.Project._

import com.typesafe.sbt.SbtScalariform._
import scalariform.formatter.preferences.{ FormattingPreferences, IndentWithTabs }

object ApplicationBuild extends Build {

	val appName	 	= "pingconf-workshop"
	val appVersion	= "1.0"

	val appDependencies = Seq(
		"com.etaty.rediscala" %% "rediscala" % "1.3"
	)

	val customSettings = Seq(
		resolvers += "rediscala" at "https://github.com/etaty/rediscala-mvn/raw/master/releases/"
	) ++ scalariformSettings ++ Seq(
		// code formatting
		ScalariformKeys.preferences := FormattingPreferences().
			setPreference(IndentWithTabs, true))


	val main = play.Project(appName, appVersion, appDependencies).settings(customSettings: _*)
}
