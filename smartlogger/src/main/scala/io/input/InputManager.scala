package input

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import fr.nicolasgille.json.{JsonFileManager, SettingType}

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

  /**
    * @inheritdoc
    */
  override def open(): Unit = {

    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    // Parse akka_setting file and bind server with good interface and port.
    val jsonManager : JsonFileManager = new JsonFileManager
    jsonManager.readFile("src/main/resources/akka_setting.json", SettingType.INPUT_AKKA_SETTING)

    // Binding the server with the interface and the port
    val serverSource = Http().bind(interface = jsonManager.inputAkkaSettings.interface, port = jsonManager.inputAkkaSettings.port)

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
        println("Accepted new connection from " + connection.remoteAddress)
        connection handleWithSyncHandler requestHandler
      }).run()
  }

}
