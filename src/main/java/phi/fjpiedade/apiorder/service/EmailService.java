package phi.fjpiedade.apiorder.service;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;

public class EmailService {
    Logger logger = LoggerFactory.getLogger(EmailService.class);

    //    @Resource(lookup = "java:jboss/mail/Default")
    @Resource(lookup = "java:jboss/mail/Default")
    private Session mailSession;

    public EmailService() {
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            InitialContext ic = new InitialContext();
            mailSession = (Session) ic.lookup("java:jboss/mail/Default");
            Message message = new MimeMessage(mailSession);
            mailSession.setDebug(true);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            logger.info("Email sent successfully");

        } catch (Exception e) {
            logger.error("Error sending email ", e);
            e.printStackTrace();
        }
    }
}
