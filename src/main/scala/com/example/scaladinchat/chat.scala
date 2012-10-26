package com.example.scaladinchat

import akka.actor._
import scala.collection.mutable

trait ChatClient {
  def onReceiveChatMessage(userName: String, msg: String)
}

object ChatActor {

  trait ChatEvent
  case class Login(channel: ChatClient, userName: String) extends ChatEvent
  case class Logout(channel: ChatClient) extends ChatEvent
  case class Message(channel: ChatClient, msg: String) extends ChatEvent
  lazy val system = ActorSystem("chatroom")
  lazy val ref = system.actorOf(Props[ChatActor])

}

class ChatActor extends Actor {

  import ChatActor._

  var members = Seq.empty[ChatClient]
  var userNames = mutable.Map.empty[ChatClient, String]

  def receive = {
    case Login(channel, userName) =>
      println("New user joined to chat")
      members = members :+ channel
      userNames += channel -> userName

    case Logout(channel) =>
      println("User disconnected")
      members = members.filterNot(_ == channel)
      userNames -= channel

    case Message(channel: ChatClient, msg) =>
      println("Got message")
      members.foreach(_.onReceiveChatMessage(userNames(channel), msg))
  }
}