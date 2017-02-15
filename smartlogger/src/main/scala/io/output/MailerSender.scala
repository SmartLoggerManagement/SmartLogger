package output

import courier.{Envelope, Mailer, Text}

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
      .startTtls(true)


    /*
     * @TODO : Fix bug on these lines.
    mailer(Envelope.from("you" `@` "gmail.com")
      .to("mom" `@` "gmail.com")
      .cc("dad" `@` "gmail.com")
      .subject("miss you")
      .content(Text(content))).onSuccess {
      case _ => println("message delivered")
    }
    */
  }
}
