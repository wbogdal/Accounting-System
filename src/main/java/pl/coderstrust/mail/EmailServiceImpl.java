package pl.coderstrust.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.coderstrust.model.Invoice;

@Component
public class EmailServiceImpl {

  private JavaMailSender javaMailSender;
  private TemplateEngine templateEngine;

  private final static Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

  public EmailServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
    this.javaMailSender = javaMailSender;
    this.templateEngine = templateEngine;
  }

  public void sendEmail(String to, String from, String replyTo, Invoice invoice) {

    Context context = new Context();
    context.setVariable("header", "New Invoice");
    context.setVariable("title", "New Invoice");
    context.setVariable("description", "New Invoice added to Account System");

    MimeMessage mail = javaMailSender.createMimeMessage();
    String body = templateEngine.process("template", context);

    try {
      MimeMessageHelper helper = new MimeMessageHelper(mail, true);
      helper.setTo(to);
      helper.setFrom(from);
      helper.setReplyTo(replyTo);
      helper.setSubject("invoice number " + invoice.getId());
      helper.setText(body, true);

    } catch (MessagingException e) {
      logger.error("Message Error", e);
    }
    javaMailSender.send(mail);
  }

  public void sendEmailByScheduled(String from, String to, String replyTo, String subject,
      String msg)
      throws MessagingException {

    MimeMessage email = javaMailSender.createMimeMessage();

    MimeMessageHelper helper = new MimeMessageHelper(email, true);
    helper.setTo(to);
    helper.setFrom(from);
    helper.setReplyTo(replyTo);
    helper.setSubject(subject);
    helper.setText(msg);

    javaMailSender.send(email);

  }
}