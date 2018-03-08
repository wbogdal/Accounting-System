package pl.coderstrust.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.coderstrust.config.ObjectMapperProvider;
import pl.coderstrust.db.impl.memory.FileHelper;
import pl.coderstrust.db.impl.memory.IdGenerator;
import pl.coderstrust.db.impl.memory.InFileDatabase;
import pl.coderstrust.db.impl.memory.JsonConverter;
import pl.coderstrust.logic.InvoiceFactory;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceItem;
import pl.coderstrust.model.Vat;


public class InFileDatabaseTest extends DatabaseTestBase {


 private FileHelper fileHelper;
 private ObjectMapperProvider objectMapperProvider;
 private JsonConverter jsonConverter;
 private InFileDatabase inFileDatabase;
 private String path = "src/test2";
 private File file = new File(path + LocalDate.now());


  @Before
  public void setUp() {
    fileHelper = new FileHelper();
    objectMapperProvider = new ObjectMapperProvider();
    jsonConverter = new JsonConverter(objectMapperProvider.mapper());
    inFileDatabase = new InFileDatabase(path, fileHelper, jsonConverter);
    super.setUp();
  }

  @After
  public void cleanUp() throws IOException {

    if (file.exists()) {
      Files.delete(Paths.get(file.getPath()));
    }
  }

  @Test
  public void shouldSaveInvoiceTest() throws IOException {

    //given

    InvoiceFactory invoiceFactory = new InvoiceFactory(inFileDatabase);

    Company seller1 = new Company();
    seller1.setName("Blue");
    seller1.setVatIdentificationNumber("6565656565555");
    Company buyer1 = new Company();
    buyer1.setName("Green");
    buyer1.setVatIdentificationNumber("7777777");
    List<InvoiceItem> invoiceItems = new ArrayList<>();
    invoiceItems.add(new InvoiceItem("Xiaomi", BigDecimal.valueOf(588888), Vat.VAT_23));

    Invoice invoice = invoiceFactory.create(LocalDate.now(), seller1, buyer1, invoiceItems);

    //when
    inFileDatabase.saveInvoice(invoice);

    //then

    assertEquals(invoice, inFileDatabase.getInvoices().get(inFileDatabase.getLastId() - 1));
  }

  @Test
  public void shouldGetInvoicesTest() throws IOException {

    //given

    InvoiceFactory invoiceFactory = new InvoiceFactory(inFileDatabase);

    Company seller1 = new Company();
    seller1.setName("Blue");
    seller1.setVatIdentificationNumber("6565656565555");
    Company buyer1 = new Company();
    buyer1.setName("Green");
    buyer1.setVatIdentificationNumber("7777777");
    List<InvoiceItem> invoiceItems = new ArrayList<>();
    invoiceItems.add(new InvoiceItem("Xiaomi", BigDecimal.valueOf(588888), Vat.VAT_23));

    Invoice invoice = invoiceFactory.create(LocalDate.now(), seller1, buyer1, invoiceItems);
    inFileDatabase.saveInvoice(invoice);

    //when
    List<Invoice> actual = inFileDatabase.getInvoices();

    //then
    assertTrue(actual.size() == 1);
  }

  @Test
  public void shouldGetLastId() {
    final InFileDatabase mock = mock(InFileDatabase.class);
    //given
    InvoiceFactory invoiceFactory = new InvoiceFactory(inFileDatabase);
    int expected = 1;
    Company seller1 = new Company();
    seller1.setName("Blue");
    seller1.setVatIdentificationNumber("6565656565555");
    Company buyer1 = new Company();
    buyer1.setName("Green");
    buyer1.setVatIdentificationNumber("7777777");
    List<InvoiceItem> invoiceItems = new ArrayList<>();
    invoiceItems.add(new InvoiceItem("Xiaomi", BigDecimal.valueOf(588888), Vat.VAT_23));

    Invoice invoice = invoiceFactory.create(LocalDate.now(), seller1, buyer1, invoiceItems);
    inFileDatabase.saveInvoice(invoice);
    //when

    int given = inFileDatabase.getLastId();
    //then
    assertEquals(expected, given);
  }

  @Override
  protected IdGenerator createIdGenerator() {
    return inFileDatabase;
  }

  @Override
  public Database createDatabase() {
    return inFileDatabase;
  }
}

