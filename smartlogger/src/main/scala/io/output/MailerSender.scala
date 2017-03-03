package output

import java.util.Properties
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.Message.RecipientType
import javax.mail.{Session, Transport}

/** MailerSender constructor.
  *  @constructor MailerSender
  *
  * @param serverAddr
  *    ServerAdress
  * @param from
  *    Sender.
  * @param tos
  *    Receivers.
  * @param ccs
  *    CCs receivers.
  * @param subject
  *    Subject to send.
  *
  */
class MailerSender(serverAddr: String, from: String, tos: Seq[String], ccs: Seq[String], subject: String) extends INotifier {

  val session = Session.getDefaultInstance(new Properties() { put("mail.smtp.host", serverAddr) })

  /**
    * @inheritdoc
    * @param text
    *   Content to send.
    */
  def send(text: String) {
    val message = new MimeMessage(session)
    message.setFrom(new InternetAddress(from))
    for (to <- tos)
      message.addRecipient(RecipientType.TO, new InternetAddress(to))
    for (cc <- ccs)
      message.addRecipient(RecipientType.TO, new InternetAddress(cc))
    message.setSubject(subject)
    message.setText(text)
    Transport.send(message)
  }
}