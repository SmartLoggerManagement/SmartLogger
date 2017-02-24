package input

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{HttpApp, Route}

/**
  *
  * @author Team SmartLogger
  * @since SmartLogger 0.1
  * SmartLogger 0.1
  *
  * @version
  */
class InputManager extends InputManagerInterface {

  override def connect(): Unit = {
    //WebServer.startServer("localhost", 8080, ServerSettings(ConfigFactory.load))
  }

  override def disconnect(): Unit ={

  }

  /**
    * Convert data received from the HTTP flux.
    *
    * @since
    * @version
    */
  override def convert(): Unit = {

  }


  /**
    * Send the data previously converted to the Machine Learning to process, train and predict method.
    *
    * @since
    * @version
    */
  override   def sendData(): Unit ={

  }

  case class Message(message : String)

  object WebServer extends HttpApp {
    def route: Route =
      path("smartlogger") {
            // At replace.
            get {
              complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
            }
          }
        /*put {
          entity(as[Message]) { message =>
            complete("The message was :" + message)
          }

        }*/
    }
  }
