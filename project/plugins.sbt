// Comment to get more information during initialization
logLevel := Level.Warn

resolvers += Classpaths.typesafeResolver

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.2.1")

// Automatic code formatting
addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.2.0")
