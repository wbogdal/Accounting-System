package pl.coderstrust.db;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import pl.coderstrust.db.impl.memory.IdGenerator;
import pl.coderstrust.logic.InvoiceFactory;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceItem;


public abstract class DatabaseTestBase {

 private Database database;
 private IdGenerator idGenerator;
 private InvoiceFactory invoiceFactory;


  @Before
  public void setUp() {
    database = createDatabase();
    idGenerator = createIdGenerator();
    invoiceFactory = new InvoiceFactory(idGenerator);
  }

  @Test

  public void shouldContainAddedInvoiceTest() throws Exception {
    //given

    Invoice invoice = invoiceFactory.create(LocalDate.of(2016, 9, 10),
        new Company(), new Company(), new ArrayList<InvoiceItem>());

    //when
    database.saveInvoice(invoice);

    //then
    assertTrue(database.getInvoices().contains(invoice));
  }

  @Test
  public void shouldRemoveInvoices() {

    //given
    Invoice invoice = invoiceFactory.create(LocalDate.of(2016, 9, 10),
        new Company(), new Company(), new ArrayList<InvoiceItem>());

    database.saveInvoice(invoice);
    //when
    database.removeInvoice(invoice.getId());
    //then
    assertTrue(database.getInvoices().isEmpty());
  }

  protected abstract IdGenerator createIdGenerator();

  public abstract Database createDatabase();
}
