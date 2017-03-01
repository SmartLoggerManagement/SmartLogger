package input

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink

import scala.concurrent.duration._
import scala.concurrent.Future

/**
  *
  * @author Team SmartLogger
  * @since SmartLogger 0.1
  * SmartLogger 0.1
  *
  * @version
  */
class InputManager extends InputManagerInterface {

  val timeout = 300.millis

  override def open(): Unit = {
    //WebServer.startServer("localhost", 8080, ServerSettings(ConfigFactory.load))
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val serverSource = Http().bind(interface = "localhost", port = 8080)

    val requestHandler: HttpRequest => HttpResponse = {
      // The PUT Request will give the logs, it needs to give them to the Batch
      case r @ HttpRequest(PUT, Uri.Path("/smartlogger"), _, _, _) =>
      val result = r.entity.toStrict(timeout).map { _.data }.map(_.utf8String)
        for {res <- result } yield {
          LogBatch.add(LogParser.parsePredictData(res))
        }
        HttpResponse(200, entity = "Data received !")

      case r: HttpRequest =>
        r.discardEntityBytes() // important to drain incoming HTTP Entity stream
        HttpResponse(404, entity = "Unknown resource!")
    }

    val bindingFuture: Future[Http.ServerBinding] =
      serverSource.to(Sink.foreach { connection =>
        println("Accepted new connection from " + connection.remoteAddress)
        connection handleWithSyncHandler requestHandler
      }).run()
  }

}
