package pl.coderstrust.db.impl.memory;

import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.Test;
import pl.coderstrust.db.Database;
import pl.coderstrust.db.DatabaseTestBase;
import pl.coderstrust.logic.InvoiceFactory;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceItem;


public class InMemoryDatabaseTest extends DatabaseTestBase {

  @Test
  public void shouldContainTwoAddedInvoiceTest() throws Exception {
    //given
    InMemoryDatabase database = new InMemoryDatabase();
    InvoiceFactory invoiceFactory = new InvoiceFactory(database);

    Invoice invoice = invoiceFactory.create(LocalDate.of(2016, 9, 10), new Company(), new Company(),
        new ArrayList<InvoiceItem>());

    Invoice invoice2 = invoiceFactory
        .create(LocalDate.of(2017, 9, 10), new Company(), new Company(),
            new ArrayList<InvoiceItem>());
  }

  @Override
  protected IdGenerator createIdGenerator() {
    return new InMemoryDatabase();
  }

  @Override
  public Database createDatabase() {
    return new InMemoryDatabase();
  }
}


