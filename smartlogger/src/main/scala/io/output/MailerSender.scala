package output

import courier.{Envelope, Mailer, Text}
import javax.mail.internet.InternetAddress
import scala.concurrent.ExecutionContext.Implicits.global

import scala.util.{Failure, Success, Try}

/**
  * Created by Kero76 on 15/02/17.
  */
class MailSender(serverAddr: String, port: Int, mailAddr: String, pwd: String) extends INotifier {

  /**
    * Stored information about mail contact
    */
  var _contactInformation : Seq[String] = Seq.empty

  /**
    *
    * @param content
    * Content to send.
    * @since SmartLogger 1.0
    * @version 1.0
    */
  override def send(content: String): Unit = {
    val mailer = Mailer(serverAddr, port)
      .auth(true)
      .as(mailAddr, pwd)
      .startTtls(true)()

    mailer(Envelope.from(new InternetAddress("you@gmail.com"))
      .to(new InternetAddress("mom@gmail.com"))
      .cc(new InternetAddress("dad@gmail.com"))
      .subject("Error on your server")
      .content(Text(content))).onComplete {
        case Success(message) => println("Message delivered : " + message)
        case Failure(message) => println("Failure during the sending of the message : "
            + message)
    }

  }
}
