package pl.coderstrust.mail;

import com.fasterxml.jackson.core.JsonProcessingException;
import javax.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.coderstrust.db.Database;
import pl.coderstrust.db.impl.memory.JsonConverter;
import pl.coderstrust.model.Invoice;

import java.util.List;

@EnableScheduling
@Component
public class EmailByScheduled {

  EmailServiceImpl emailService;
  JsonConverter jsonConverter;
  Database database;

  private final static Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

  @Autowired
  public EmailByScheduled(EmailServiceImpl emailService,
      JsonConverter jsonConverter, Database database) {

    this.emailService = emailService;
    this.jsonConverter = jsonConverter;
    this.database = database;

  }

  private String invoiceConverterByEmail(JsonConverter jsonConverter, Database database) {

    List<Invoice> invoicesFromCurrentDay = database.getInvoicesFromCurrentDay();
    StringBuilder stringBuilder = new StringBuilder();
    if (!invoicesFromCurrentDay.isEmpty()) {
      invoicesFromCurrentDay.forEach(invoice -> {
        try {
          stringBuilder.append(jsonConverter.convertFromInvoiceToJson(invoice));
        } catch (JsonProcessingException e) {
          logger.error("Json Processing Error", e);
        }
      });

      return stringBuilder.toString();

    } else {
      return null;
    }
  }

  @Scheduled(cron = " * * 1 * * *", zone = "CET")
  public void sendMailAtTime() throws MessagingException {
    if (invoiceConverterByEmail(jsonConverter, database) != null) {

      emailService.sendEmailByScheduled("hawlik1989@gmail.com", "hawlik1989@gmail.com",
          "hawlik1989@gmail.com", "Invoice From Current Day", "Invoice From Current Day \n"
              + invoiceConverterByEmail(jsonConverter, database));
    }
  }

}