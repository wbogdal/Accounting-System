package pl.coderstrust.db.impl.memory;

import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import pl.coderstrust.logic.InvoiceFactory;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceItem;
import pl.coderstrust.model.Vat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class MongoDatabaseTest {

  private ObjectMapper objectMapper;
  private JsonConverter jsonConverter;
  private DBObjectConverter dbObjectConverter;
  private MongoDatabase mongoDatabase;


  @Before
  public void setUp() {

  }

  @Test
  public void saveInvoice() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    JsonConverter jsonConverter = new JsonConverter(objectMapper);
    DBObjectConverter dbObjectConverter = new DBObjectConverter();
    MongoDatabase mongoDatabase = new MongoDatabase(jsonConverter, dbObjectConverter);

    //given
    Company seller1 = new Company();
    seller1.setName("Blue");
    seller1.setVatIdentificationNumber("6565656565555");
    Company buyer1 = new Company();
    buyer1.setName("Green");
    buyer1.setVatIdentificationNumber("7777777");
    List<InvoiceItem> invoiceItems = new ArrayList<>();
    invoiceItems.add(new InvoiceItem("Xiaomi", BigDecimal.valueOf(588888), Vat.VAT_23));
    invoiceItems.add(new InvoiceItem("Budomi", BigDecimal.valueOf(65), Vat.VAT_23));

    InvoiceFactory invoiceFactory = new InvoiceFactory(mongoDatabase);

    Invoice invoice = invoiceFactory.create(LocalDate.now(), seller1, buyer1, invoiceItems);

    //when
    mongoDatabase.saveInvoice(invoice);

    //then

    assertTrue(mongoDatabase.getInvoices().contains(invoice));

    mongoDatabase.removeInvoice(invoice.getId());
  }

  @Test
  public void removeInvoice() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    JsonConverter jsonConverter = new JsonConverter(objectMapper);
    DBObjectConverter dbObjectConverter = new DBObjectConverter();
    MongoDatabase mongoDatabase = new MongoDatabase(jsonConverter, dbObjectConverter);

    //given
    Company seller1 = new Company();
    seller1.setName("Blue");
    seller1.setVatIdentificationNumber("6565656565555");
    Company buyer1 = new Company();
    buyer1.setName("Green");
    buyer1.setVatIdentificationNumber("7777777");
    List<InvoiceItem> invoiceItems = new ArrayList<>();
    invoiceItems.add(new InvoiceItem("Xiaomi", BigDecimal.valueOf(588888), Vat.VAT_23));
    invoiceItems.add(new InvoiceItem("Budomi", BigDecimal.valueOf(65), Vat.VAT_23));

    InvoiceFactory invoiceFactory = new InvoiceFactory(mongoDatabase);

    Invoice invoice = invoiceFactory.create(LocalDate.now(), seller1, buyer1, invoiceItems);
    mongoDatabase.saveInvoice(invoice);
    //when
    mongoDatabase.removeInvoice(invoice.getId());
//then

    assertTrue(!mongoDatabase.getInvoices().contains(invoice));
  }
}