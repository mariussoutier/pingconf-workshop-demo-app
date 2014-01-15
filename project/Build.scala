import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "forms"
    val appVersion      = "1.0"

    val appDependencies = Seq(
  	 "com.etaty.rediscala" %% "rediscala" % "1.2"
    )

   
	
    val main = play.Project(appName, appVersion, appDependencies).settings(
       resolvers += "rediscala" at "https://github.com/etaty/rediscala-mvn/raw/master/releases/"
    )

}
