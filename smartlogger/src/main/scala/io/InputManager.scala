import akka.http.scaladsl.model.{ ContentTypes, HttpEntity }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.HttpApp
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.settings.ServerSettings
import com.typesafe.config.ConfigFactory

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
    WebServer.startServer("localhost", 8080, ServerSettings(ConfigFactory.load))
  }

  override def disconnect(): Unit ={

  }

  /**
    * Convert data receive from the HTTP flux.
    *
    * @since
    * @version
    */
  override def convert(): Unit = {

  }


  /**
    * Send the data previously convert to the Machine Learning to process train and predict method.
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
        put {
          entity(as[Message]) { message =>
            complete("The message was :" + message)
          }

        }
      }
  }

  }
