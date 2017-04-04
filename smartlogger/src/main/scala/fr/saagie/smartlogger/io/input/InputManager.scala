package fr.saagie.smartlogger.io.input

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import fr.saagie.smartlogger.utils.Properties

/**
  * Standard implementation of the InputManager
  *
  * @author Jordan Baudin
  * @since SmartLogger 0.1
  * @version 1.0
  */
class InputManager extends IInputManager {
  // ATTRIBUTES
  private val timeout = 300.millis

  private var serverSource: Source[Http.IncomingConnection, Future[Http.ServerBinding]] = _

  private implicit var system: ActorSystem = ActorSystem("SmartLogger")
  private implicit var materializer: ActorMaterializer = ActorMaterializer()
  private implicit val executionContext = system.dispatcher

  // COMMANDS
  /**
    * @inheritdoc
    */
  override def open(): Unit = {
    // Checks if the server is available
    if (serverSource != null) {
      println("The server is already open")
      return
    }

    system = ActorSystem("SmartLogger")
    materializer = ActorMaterializer()

    // Binding the server with the interface and the port
    serverSource = Http().bind(Properties.AKKA.get("interface"), Integer.valueOf(Properties.AKKA.get("port")))

    // Defining the requestHandler to get the logs received from HTTP requests
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

    // Running the server
    val bindingFuture: Future[Http.ServerBinding] =
      serverSource.to(Sink.foreach { connection =>
        connection handleWithSyncHandler requestHandler
      }).run()
  }

  /**
    * @inheritdoc
    */
  def close(): Unit = {
    materializer.shutdown()
    Await.result(system.terminate(), 1 second)
    serverSource = null
  }
}
