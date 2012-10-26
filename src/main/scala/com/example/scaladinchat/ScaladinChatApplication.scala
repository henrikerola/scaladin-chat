package com.example.scaladinchat

import vaadin.scala._
import vaadin.scala.Alignment._
import vaadin.scala.AbstractTextField.TextChangeEventMode._
import ChatActor._

class ScaladinChatApplication extends Application with ChatClient { app =>

  def onReceiveChatMessage(userName: String, msg: String) {
    val itemId = table.addItem.get
    table.property(itemId, "User").get.value = userName
    table.property(itemId, "Message").get.value = msg
  }

  class LoginPopup extends Window { popup =>
    caption = "Login into chat"
    modal = true
    resizable = false
    closable = false
    size(200 px, 100 px)

    content = new VerticalLayout {
      margin = true
      spacing = true

      val userNameField = add(new TextField)
      add(Button("Login", { e =>
        val userName = userNameField.value.getOrElse("").toString
        if (userName.isEmpty) mainWindow.showNotification("User name must be given")
        else {
          ChatActor.ref ! Login(app, userName)
          mainWindow.childWindows -= popup
        }
      }))
    }
  }

  override def init() {
    super.init()
    mainWindow.childWindows += new LoginPopup
  }

  override def close() {
    ChatActor.ref ! Logout(app)
    super.close()
  }

  val table = new Table {
    sizeFull()
    addContainerProperty("User", classOf[String], None)
    addContainerProperty("Message", classOf[String], None)
  }

  override def main = new VerticalLayout {
    margin = true
    spacing = true
    sizeFull()

    add(new HorizontalLayout {
      width = 100 pct

      add(new ProgressIndicator)
      add(Button("Logout", close()), alignment = TopRight)
    })

    add(table, ratio = 1)

    add(new HorizontalLayout {
      spacing = true
      width = 100 pct

      val msgField: TextField = add(new TextField {
        width = 100 pct

        textChangeListeners += { e =>
          sendButton.enabled = !e.text.isEmpty
        }
      }, ratio = 1)

      val sendButton = add(new Button {
        caption = "Send"
        enabled = false
        clickShortcut = KeyShortcut(KeyCode.Enter)

        clickListeners += { e =>
          val msg = msgField.value.getOrElse("").toString
          if (!msg.isEmpty) {
            ChatActor.ref ! Message(app, msg)
            msgField.value = ""
            enabled = false
          }
        }
      })
    })
  }
}