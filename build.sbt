name := "scaladin-chat"

scalaVersion := "2.10.2"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
//resolvers += "Vaadin add-ons repository" at "http://maven.vaadin.com/vaadin-addons"

resolvers += "Scaladin Snapshots" at "http://henrikerola.github.io/repository/snapshots/"

libraryDependencies ++= Seq(
  "com.vaadin" % "vaadin-server" % "7.1.3",
  "com.vaadin" % "vaadin-themes" % "7.1.3" % "container",
  "com.vaadin" % "vaadin-client-compiled" % "7.1.3" % "container",
  "com.vaadin" % "vaadin-push" % "7.1.3", //% "container",
  "vaadin.scala" %% "scaladin" % "3.0-SNAPSHOT",
  "com.typesafe.akka" %% "akka-actor" % "2.2.1",
  "org.eclipse.jetty" % "jetty-webapp" % "8.1.12.v20130726" % "container",
  "org.eclipse.jetty" % "jetty-websocket" % "8.1.12.v20130726" % "container"
)

vaadinWebSettings
