package smartlogger.src.main.scala.io

import scala.concurrent.Future
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._


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
    implicit val system = ActorSystem()

    implicit val materializer = ActorMaterializer()

    implicit val executionContext = system.dispatcher

    val serverSource: Source[Http.IncomingConnection, Future[Http.ServerBinding]] =
      Http().bind(interface = "localhost", port = 8080)

    val bindingFuture: Future[Http.ServerBinding] =
      serverSource.to(Sink.foreach { connection =>
        //ici recuperer le contenu de requete et les renvoyer
      }).run()
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


  }
