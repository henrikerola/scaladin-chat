package com.example.scaladinchat

import vaadin.scala._
import vaadin.scala.server._
import vaadin.scala.Alignment._
import ChatActor._

class ScaladinChatUI extends UI with ChatClient { app =>

  pushConfiguration.pushMode = PushMode.Automatic

  def onReceiveChatMessage(userName: String, msg: String) {
    access {
      val itemId = table.addItem()
      table.getProperty(itemId, "User").value = userName
      table.getProperty(itemId, "Message").value = msg
    }
  }

  class LoginPopup extends Window { popup =>
    caption = "Login into chat"
    modal = true
    resizable = false
    closable = false
    size(200 px, 125 px)

    content = new VerticalLayout {
      margin = true
      spacing = true

      val userNameField = add(new TextField)
      add(Button("Login", { e =>
        val userName = userNameField.value getOrElse ""
        if (userName.isEmpty) {
          Notification.show("User name must be given")
        } else {
          ChatActor.ref ! Login(app, userName)
          windows -= popup
        }
      }))
    }
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

  content = new VerticalLayout {
    margin = true
    spacing = true
    sizeFull()

    add(Button("Logout", close()), alignment = TopRight)

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
          val msg = msgField.value getOrElse ""
          if (!msg.isEmpty) {
            ChatActor.ref ! Message(app, msg)
            msgField.value = ""
            enabled = false
          }
        }
      })
    })

    windows += new LoginPopup
  }
}