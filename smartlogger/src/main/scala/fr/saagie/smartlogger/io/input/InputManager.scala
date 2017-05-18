package fr.saagie.smartlogger.io.input

import java.io._

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import fr.saagie.smartlogger.SmartLogger

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import fr.saagie.smartlogger.utils.Properties

/**
  * Standard implementation of the InputManager
  *
  * Handled requests :
  *   (GET)
  *   - /smartlogger/properties/{name} : Returns the property file associated with the given "name"
  *
  *   (PUT)
  *   - /smartlogger/analyze : Analyzes all logs provided by the request
  *   - /smartlogger/provide : Saves all labelled logs provided by the request
  *   - /smartlogger/train : An order to force the system to train
  *   - /smartlogger/properties/{name} : Alter the content of the associated property file
  *
  * @author Jordan BAUDIN, Franck CARON
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
      // The PUT Request to analyze logs
      case r @ HttpRequest(PUT, Uri.Path("/smartlogger/analyze"), _, _, _) => analyze(r)

      // The PUT Request to store training data in the system
      case r @ HttpRequest(PUT, Uri.Path("/smartlogger/provide"), _, _, _) => provide(r)

      // The PUT Request to train the system
      case r @ HttpRequest(PUT, Uri.Path("/smartlogger/train"), _, _, _) => train(r)

      // The PUT Request to alter a properties file
      case r @ HttpRequest(PUT, uri, _, _, _)
        if uri.path.startsWith(Uri.Path("/smartlogger/properties")) => putProperties(r)

      // The GET Request to read a properties file
      case r @ HttpRequest(GET, uri, _, _, _)
        if uri.path.startsWith(Uri.Path("/smartlogger/properties") )=> getProperties(r)

      // Unknown request type
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


  // ACTIONS
  /**
    * Handles /smartlogger/analyze PUT requests
    * Adds all logs received to the batch, in order to be analyzed later.
    */
  def analyze(httpRequest: HttpRequest): HttpResponse = {
    val result = httpRequest.entity.toStrict(timeout).map { _.data }.map(_.utf8String)
    for {res <- result } yield {
      LogBatch.add(LogParser.parsePredictData(res))
    }

    HttpResponse(200, entity = "Data received ! Sent to be analyzed")
  }

  /**
    * Handles /smartlogger/provide PUT requests
    * Adds all logs received to the database, as samples for coming trainings.
    */
  def provide(httpRequest: HttpRequest): HttpResponse = {
    val result = httpRequest.entity.toStrict(timeout).map { _.data }.map(_.utf8String)
    for {res <- result } yield {
      val logs = LogParser.parseTrainingData(res)

      // Inserting new parsed logs into the database
      for ((content, label) <- logs) {
        val log = SmartLogger.DAO.newInstance()
        log.setLabel(label)
        log.setContent(content)

        SmartLogger.DAO.insert(log)
      }
    }

    HttpResponse(200, entity = "Data received ! Saved for future trainings")
  }

  /**
    * Handles /smartlogger/train PUT requests
    * Adds all logs received to the batch, in order to be analyzed later.
    */
  def train(httpRequest: HttpRequest): HttpResponse = {
    SmartLogger.trainSystem()
    HttpResponse(200, entity = "Order received ! The system has been trained")
  }

  /**
    * Handles /smartlogger/properties/{name} PUT requests
    * Returns all logs received to the batch, in order to be analyzed later.
    */
  def putProperties(httpRequest: HttpRequest): HttpResponse = {
    // Extracting filename
    val path = httpRequest.uri.path.toString()
    val filename = path.split("/").last

    // Writing content of .properties file
    try {
      val writer = new BufferedWriter(new FileWriter(new File(Properties.DIRECTORY + filename)))
      val result = httpRequest.entity.toStrict(timeout).map { _.data }.map(_.utf8String)
      for {res <- result } yield {
        writer.write(res)
      }
      writer.close()
    } catch {
      case e:Exception => return HttpResponse(404, entity = "Properties file not found")
    }

    // Returning answer
    HttpResponse(200, entity = "File received ! The properties has been updated")
  }

  /**
    * Handles /smartlogger/properties/{name} GET requests
    * Returns all logs received to the batch, in order to be analyzed later.
    */
  def getProperties(httpRequest: HttpRequest): HttpResponse = {
    // Extracting filename
    val path = httpRequest.uri.path.toString()
    val filename = path.split("/").last

    // Reading content of .properties file
    val content = new StringBuilder
    try {
      val reader = new BufferedReader(new FileReader(new File(Properties.DIRECTORY + filename)))
      val it = reader.lines().iterator()
      while (it.hasNext) {
        content.append(it.next())
      }
      reader.close()
    } catch {
      case e:Exception => return HttpResponse(404, entity = "Properties file not found")
    }

    // Returning answer
    HttpResponse(200, entity = content.toString)
  }
}
