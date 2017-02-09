import scala.concurrent.Future

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

/**
  *
  * @author
  * @since SmartLogger 0.1
  * @version
  */
class InputManager {
  // Connect

  // Modifie les données => Transforme contenu.

  // Envoie les odnnées au ML.

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val serverSource: Source[Http.IncomingConnection, Future[Http.ServerBinding]] =
    Http().bind(interface = "localhost", port = 8080)
  val bindingFuture: Future[Http.ServerBinding] =
    serverSource.to(Sink.foreach { connection => // foreach materializes the source
      println("Accepted new connection from " + connection.remoteAddress)
      // ... and then actually handle the connection
    }).run()
}
