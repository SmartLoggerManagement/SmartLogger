package fr.saagie.smartlogger.io.output.notifier

import java.util.Date
import javax.mail
import javax.mail.Message.RecipientType
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Authenticator, PasswordAuthentication, Session, Transport}

import fr.saagie.smartlogger.utils.Properties

/**
  * Defines a notifier which emits its messages through mail.
  *
  * @author Franck Caron, Nicolas Gille
  * @since SmartLogger 0.1
  * @version 1.0
  */
class MailNotifier(subject: String) extends INotifier {
  // ALTERNATIVES
  def this() = this("SmartLogger - Notification")

  // COMMANDS
  /**
    * Method used to send email.
    *
    * @since SmartLogger 0.1
    * @version 1.0
    */
  override def send() {
    // Init. of the message
    val props = System.getProperties
    props.put("mail.smtp.auth", "true")
    props.put("mail.smtp.starttls.enable", "true")
    props.put("mail.smtp.host", Properties.MAIL.get("host"))
    props.put("mail.smtp.port", Properties.MAIL.get("port"))

    // Building message
    val session = Session.getInstance(props, new Authenticator {
      override def getPasswordAuthentication: PasswordAuthentication =
        new mail.PasswordAuthentication(Properties.MAIL.get("address"), Properties.MAIL.get("$password"))
    })
    session.setDebug(true) // Comment line when code working.

    val message = new MimeMessage(session)
    message.setFrom(new InternetAddress(Properties.MAIL.get("address")))

    // Adding recipients
    for (to <- getRecipients()) message.addRecipient(RecipientType.TO, new InternetAddress(to))

    // Defining message content
    message.setSubject(subject)
    message.setText(getText())
    message.setSentDate(new Date())
    message.saveChanges()

    // Sending message
    Transport.send(message)
  }
}