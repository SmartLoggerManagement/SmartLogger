
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.util.ByteString

import scala.concurrent.Future


class ClientTest {
  def sendRequest(content : String, uri : String ) {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val data: ByteString = ByteString.fromString(content)
  val responseFuture: Future[HttpResponse] =
    Http().singleRequest(HttpRequest(PUT, uri = uri, entity = HttpEntity(data)))
  //"http://127.0.0.1:8080/smartlogger"
  //println(responseFuture)
  }

  def sendPost(content : String, uri : String ) {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()

    val data: ByteString = ByteString.fromString(content)
    val responseFuture: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(POST, uri = uri, entity = HttpEntity(data)))
    //"http://127.0.0.1:8080/smartlogger"
    //println(responseFuture)
  }
}

