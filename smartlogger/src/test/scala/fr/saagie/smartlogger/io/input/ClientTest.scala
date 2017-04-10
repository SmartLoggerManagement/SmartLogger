package fr.saagie.smartlogger.io.input

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.http.scaladsl.settings.ConnectionPoolSettings
import akka.stream.ActorMaterializer
import akka.util.ByteString

import scala.concurrent.duration._
import scala.concurrent.Future

/**This class send a request with a given body to a given uri
  *
  * @author Grégoire POMMIER
  * @since SmartLogger 0.1
  * @version 1.0
  */
class ClientTest {
  /**
   * This method send a put request
   */
  /**
    * @TODO Add comments
    */
  def sendPutRequest(content : String, uri : String ) {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val data: ByteString = ByteString.fromString(content)

  val connectionPoolSettings = ConnectionPoolSettings(system).withIdleTimeout(2 second)

  val responseFuture: Future[HttpResponse] =
    Http().singleRequest(HttpRequest(PUT, uri = uri, entity = HttpEntity(data)), settings = connectionPoolSettings)
  }
  /**
    * This method send a post request
    */
  /**
    * @TODO Add comments
    */
  def sendPostRequest(content : String, uri : String ) {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()

    val data: ByteString = ByteString.fromString(content)
    val responseFuture: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(POST, uri = uri, entity = HttpEntity(data)))
  }
}

