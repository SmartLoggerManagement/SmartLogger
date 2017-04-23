package fr.saagie.smartlogger.ihm

import fr.saagie.smartlogger.utils.Properties
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.Includes._
import scalafx.geometry.Pos
import javafx.geometry.Insets
import scalafx.scene.image._
import scalafx.scene.layout.{HBox, VBox}

/**
  * Defines an interface 
  *
  * @author Khalid RABOU
  * @since SmartLogger 0.1
  * @version 1.0
  */

object Ihm extends JFXApp {


  stage = new PrimaryStage {
    title = "SmartLogger"
    width=700
    height=550
    scene = new Scene {


      content =  new VBox(10,
          new ImageView(new Image("logo.png")) ,
        new TabPane{


          val principaleTab = new Tab{ text="Principale"; closable=false}
          val parametrageTab = new Tab{ text="Paramétrage"; closable=false}

          parametrageTab.content = new TabPane{
        // AKKA
            // Akka Tab initialisation
            val akkaTab = new Tab{ text="Akka"; closable=false}
            val textInterface = new TextField{text=Properties.AKKA.get("interface")}
            val textPort = new TextField{text=Properties.AKKA.get("port")}
            val akkaButton = new Button("Enregistrer")
            // Akka button event handler
            akkaButton.onAction = handle {
              Properties.AKKA.set("interface", textInterface.text.value)
              Properties.AKKA.set("port", textPort.text.value)
              Properties.AKKA.save()
            }
            // Akka VBox
            akkaTab.content=new VBox(10,
              new HBox(10,new Label("Interface"),textInterface),
              new HBox(10,new Label("Port"),textPort),
              akkaButton
            )
          // DB
            // DB Tab initialisation
            val dbTab = new Tab{ text="Database"; closable=false}
            val textdbname =new TextField{text=Properties.DB.get("dbname")}
            val textDriver =new TextField{text=Properties.DB.get("driver")}
            val textUrl =new TextField{text=Properties.DB.get("url")}
            val textUsername =new TextField{text=Properties.DB.get("username")}
            val textPassword =new TextField{text=Properties.DB.get("password")}
            val dbButton = new Button("Enregistrer")
            // DB button event handler
            dbButton.onAction = handle {
              Properties.DB.set("dbname", textdbname.text.value)
              Properties.DB.set("driver", textDriver.text.value)
              Properties.DB.set("url", textUrl.text.value)
              Properties.DB.set("username", textUsername.text.value)
              Properties.DB.set("password", textPassword.text.value)
              Properties.DB.save()
            }
            // DB VBox
            dbTab.content=new VBox(10,
              new HBox(10,new Label("dbname"),textdbname),
              new HBox(10,new Label("Driver"),textDriver),
              new HBox(10,new Label("Url"),textUrl),
              new HBox(10,new Label("Username"),textUsername),
              new HBox(10,new Label("Password"),textPassword),
              dbButton)
          // DB_TEST
            // DB_TEST Tab initialisation
            val dbTestTab = new Tab{ text="Database"; closable=false}
            val textDriverT =new TextField{text=Properties.DB_TEST.get("driver")}
            val textUrlT =new TextField{text=Properties.DB_TEST.get("url")}
            val textUsernameT =new TextField{text=Properties.DB_TEST.get("username")}
            val textPasswordT =new TextField{text=Properties.DB_TEST.get("password")}
            val dbTestButton = new Button("Enregistrer")
            // DB_TEST button event handler
            dbTestButton.onAction = handle {
              Properties.DB_TEST.set("driver", textDriverT.text.value)
              Properties.DB_TEST.set("url", textUrlT.text.value)
              Properties.DB_TEST.set("username", textUsernameT.text.value)
              Properties.DB_TEST.set("password", textPasswordT.text.value)
              Properties.DB_TEST.save()
            }
            // DB_TEST VBox
            dbTestTab.content=new VBox(10,
              new HBox(10,new Label("Driver"),textDriverT),
              new HBox(10,new Label("Url"),textUrlT),
              new HBox(10,new Label("Username"),textUsernameT),
              new HBox(10,new Label("Password"),textPasswordT),
              dbTestButton)

          // Mail
            // Mail Tab initialisation
            val mailTab = new Tab{ text="Mail"; closable=false}
            val textHost = new TextField{text=Properties.MAIL.get("host")}
            val textAddress =new TextField{text=Properties.MAIL.get("address")}
            val textPasswordM =new TextField{text=Properties.MAIL.get("$password")}
            val textPortM =new TextField{text=Properties.MAIL.get("port")}
            val textSubject =new TextField{text=Properties.MAIL.get("subject")}
            val textContact =new TextField{text=Properties.MAIL.get("contact")}
            val mailButton =new Button("Enregistrer")
            // MAIL button event handler
            mailButton.onAction = handle {
              Properties.MAIL.set("host", textHost.text.value)
              Properties.MAIL.set("address", textAddress.text.value)
              Properties.MAIL.set("$password", textPasswordM.text.value)
              Properties.MAIL.set("port", textPortM.text.value)
              Properties.MAIL.set("subject", textSubject.text.value)
              Properties.MAIL.set("contact", textContact.text.value)
              Properties.MAIL.save()
            }
            // MAIL VBox
            mailTab.content=new VBox(10,
              new HBox(10,new Label("Host"),textHost),
              new HBox(10,new Label("Address"),textAddress),
              new HBox(10,new Label("Password"),textPasswordM),
              new HBox(10,new Label("Port"),textPortM),
              new HBox(10,new Label("Subject"),textSubject),
              new HBox(10,new Label("Contact"),textContact),
              mailButton)
          // Slack
            // Slack Tab initialisation
            val slackTab = new Tab{ text="Slack"; closable=false}
            val textApiKey =new TextField{text=Properties.SLACK.get("$apiKey")}
            val textThread =new TextField{text=Properties.SLACK.get("thread")}
            val textContactS =new TextField{text=Properties.SLACK.get("contact")}
            val slackButton = new Button("Enregistrer")
            // Slack button event handler
            slackButton.onAction = handle {
              Properties.SLACK.set("$apiKey", textApiKey.text.value)
              Properties.SLACK.set("thread", textThread.text.value)
              Properties.SLACK.set("contact", textContactS.text.value)
              Properties.SLACK.save()
            }
            // SLACK VBox
            slackTab.content=new VBox(10,
              new HBox(10,new Label("$apiKey"),textApiKey),
              new HBox(10,new Label("Thread"),textThread),
              new HBox(10,new Label("Contact"),textContactS),
              slackButton)
            tabs=List(akkaTab,dbTab,dbTestTab,mailTab,slackTab)}
          tabs=List(principaleTab,parametrageTab)
        }
        ){
        alignment = Pos.Center

        padding = new Insets(20,0,0,100)
        spacing = 20
      }

    }


  }
}