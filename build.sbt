name := "scaladin-chat"

resolvers += "Vaadin add-ons repository" at "http://maven.vaadin.com/vaadin-addons"

libraryDependencies ++= Seq(
  "com.vaadin" % "vaadin" % "6.8.4",
  "org.vaadin.addons" % "scaladin" % "2.1.0",
  "com.typesafe.akka" % "akka-actor" % "2.0.3",
  "org.eclipse.jetty" % "jetty-webapp" % "8.1.7.v20120910" % "container"
)

seq(webSettings :_*)
