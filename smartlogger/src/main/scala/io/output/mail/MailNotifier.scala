package output.mail

import java.util.Date
import javax.mail
import javax.mail.Message.RecipientType
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Authenticator, PasswordAuthentication, Session, Transport}

import output.Notifier

/**
  * Defines global parameters which aren't modified between all MailAlerter instances
  */
object MailNotifier {
  /** The address of the server */
  val serverAddress:String = "smtp.gmail.com"

  /** The sender's mail adress used for sending any mail from this notifier */
  val from:String = "smartlogger@saagie.com"
}

/**
  * Defines a notifier which emits its messages through mail.
  */
class MailNotifier(subject:String) extends Notifier {
  // ALTERNATIVES
  def this() = this("SmartLogger - Notification")


  // COMMANDS
  override def send() {
    // Init. of the message
    val prop = System.getProperties
    prop.put("mail.smtp.host", MailNotifier.serverAddress)
    prop.put("mail.smtp.auth", "true")

    // Building message
    val session = Session.getInstance(prop, new Authenticator {
      override def getPasswordAuthentication: PasswordAuthentication =
        new mail.PasswordAuthentication("smartlogger", "$martlogger")
    })

    session.setDebug(true)
    val message = new MimeMessage(session)
    message.setFrom(new InternetAddress(MailNotifier.from))

    // Adding recipients
    for (to <- recipients) message.addRecipient(RecipientType.TO, new InternetAddress(to))

    // Defining message content
    message.setSubject(subject)
    message.setText(text)
    message.setSentDate(new Date())
    message.saveChanges()

    println("before sending")

    // Sending message
    val tr = session.getTransport("smtp")
    tr.connect(MailNotifier.serverAddress, "smartlogger", "$martlogger")
    tr.sendMessage(message, message.getAllRecipients)
    Transport.send(message)


    println("See mailbox")
  }
}