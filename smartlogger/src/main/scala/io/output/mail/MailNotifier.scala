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
    val props = System.getProperties
    props.put("mail.smtp.auth", "true")
    props.put("mail.smtp.starttls.enable", "true")
    props.put("mail.smtp.host", "smtp.gmail.com")
    props.put("mail.smtp.port", "587")

    val username = "smartlogger76@gmail.com"
    val password = "$martlogger"

    // Building message
    val session = Session.getInstance(props, new Authenticator {
      override def getPasswordAuthentication: PasswordAuthentication =
        new mail.PasswordAuthentication(username, password)
    })
    session.setDebug(true) // Comment line when code working.

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
    Transport.send(message)

    println("See mailbox")
  }
}