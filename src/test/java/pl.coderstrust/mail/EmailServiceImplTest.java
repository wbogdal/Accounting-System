package pl.coderstrust.mail;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.coderstrust.model.Invoice;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class EmailServiceImplTest {

  @Autowired
  private EmailServiceImpl emailService;

  @Rule
  public SmtpServerRule smtpServerRule = new SmtpServerRule(587);

  @Test
  public void shouldSendEmail() throws MessagingException, IOException {
    emailService.sendEmailByScheduled("hawlik1989@gmail.com", "hawlik1989@gmail.com",
        "hawlik1989@gmail.com", "Test Mail", "Email Service Test");

    MimeMessage[] receivedMessages = smtpServerRule.getMessages();
    assertEquals(0, receivedMessages.length);
  }

  @Test
  public void shouldSendEmailWithInvoice() throws MessagingException, IOException {
    Invoice invoice = new Invoice();
    emailService
        .sendEmail("hawlik1989@gmail.com", "hawlik1989@gmail.com", "hawlik1989@gmail.com", invoice);

    MimeMessage[] receivedMessages = smtpServerRule.getMessages();
    assertEquals(0, receivedMessages.length);
  }


}