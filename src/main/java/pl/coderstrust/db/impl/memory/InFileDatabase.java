package pl.coderstrust.db.impl.memory;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import pl.coderstrust.db.Database;
import pl.coderstrust.model.Invoice;

@Repository
public class InFileDatabase implements Database, IdGenerator {


  private final File file;
  private final FileHelper fileHelper;
  private final JsonConverter converter;
  private final AtomicInteger counter = new AtomicInteger();

  private final static Logger logger = LoggerFactory.getLogger(InFileDatabase.class);

  @Autowired
  public InFileDatabase(@Value("${database.path}") String path, FileHelper fileHelper,
      JsonConverter converter) {
    this.file = new File(path + LocalDate.now());
    this.fileHelper = fileHelper;
    this.converter = converter;
  }

  @Override
  public void saveInvoice(Invoice invoice) {
    List<String> listInJson = new ArrayList();
    try {
      listInJson.add(converter.convertFromInvoiceToJson(invoice));
    } catch (JsonProcessingException e) {
      logger.error("Json Processing Error" + file.getName(), e);
    }

    if (file.exists() && !file.isDirectory()) {
      try {
        fileHelper.appendInvoice(listInJson, file);
      } catch (IOException e) {
        logger.error("Append Invoice Error" + file.getName(), e);
      }
    } else {
      try {
        fileHelper.saveNewInvoice(listInJson, file);
      } catch (IOException e) {
        logger.error("Save New Invoice Error" + file.getName(), e);
      }
    }
  }

  @Override
  public void removeInvoice(int id) {
    List<String> invoices = null;
    try {
      invoices = fileHelper.readFromFile(file);
    } catch (IOException e) {
      logger.error("Not read File" + file.getName(), e);
    }
    List<String> invoicesAfterRemoval = new ArrayList();
    for (String line : invoices) {
      Invoice invoice = converter.convertFromJsonToInvoice(line);
      if (invoice.getId() != id) {
        try {
          invoicesAfterRemoval.add(converter.convertFromInvoiceToJson(invoice));
        } catch (JsonProcessingException e) {
          logger.error("Json Processing Error" + file.getName(), e);
        }
      }
    }
    try {
      fileHelper.saveNewInvoice(invoicesAfterRemoval, file);
    } catch (IOException e) {
      logger.error("Save New Invoice Error" + file.getName(), e);
    }
  }

  @Override
  public List<Invoice> getInvoices() {
    try {
      return fileHelper.readFromFile(file).stream()
          .map(line -> converter.convertFromJsonToInvoice(line)).sorted()
          .collect(Collectors.toList());
    } catch (IOException e) {
      logger.warn("Not read from file" + file.getName(), e);
    }
    return null;
  }

  @Override
  public List<Invoice> getInvoicesFromCurrentDay() {
    try {
      fileHelper.readFromFile(file);
    } catch (IOException e) {
      logger.warn("Not read from file" + file.getName(), e);
    }

    return getInvoices().stream().filter(invoice -> invoice.getLocalDate().isEqual(LocalDate.now()))
        .collect(Collectors.toList());
  }

  public int generate() {
    counter.set(getLastId());
    return counter.incrementAndGet();
  }

  public int getLastId() {
    List<Invoice> listOfInvoices = getInvoices();
    if (!listOfInvoices.isEmpty()) {
      return listOfInvoices.get(listOfInvoices.size() - 1).getId();
    } else {
      return 0;
    }
  }

}