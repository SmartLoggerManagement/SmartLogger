package output

import courier.{Envelope, Mailer, Text}
import javax.mail.internet.InternetAddress

import scala.util.{Failure, Success, Try}

/**
  * Created by Kero76 on 15/02/17.
  */
class MailSender(serverAddr: String, port: Int, mailAddr: String, pwd: String) extends IOutputManager {

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


    //@TODO : Change onSucess to a updated function from Future.
    mailer(Envelope.from(new InternetAddress("you@gmail.com"))
      .to(new InternetAddress("mom@gmail.com"))
      .cc(new InternetAddress("dad@gmail.com"))
      .subject("Error on your server")
      .content(Text(content))).onSuccess {
      case _ => println("message delivered")
    }

  }
}
