/**
  * Created by Camille on 24/02/17.
  * Revision:
  * Comments : needs revision
  */

import akka.http.javadsl.model._;
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

import scala.concurrent.Future
import scala.util.{ Failure, Success }

class AutomaticClient {

  def main(): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val connectionFlow: Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]] =
      Http().outgoingConnection("akka.io")
    val responseFuture: Future[HttpResponse] =
      Source.single(HttpRequest(uri = "/"))
        .via(connectionFlow)
        .runWith(Sink.head)

    responseFuture.andThen {
      case Success(_) => println("request succeded")
      case Failure(_) => println("request failed")
    }.andThen {
      case _ =>
        Uri testUri = uri.create("/test");
        ByteString data = ByteString.fromString("I am data!");
        HttpRequest testPostRequest = HttpRequest.POST(testUri).withEntity(data);
    }
  }
}
