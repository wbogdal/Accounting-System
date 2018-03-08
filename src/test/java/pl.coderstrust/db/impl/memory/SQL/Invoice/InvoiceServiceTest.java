package pl.coderstrust.db.impl.memory.SQL.Invoice;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceItem;
import pl.coderstrust.model.Vat;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest
public class InvoiceServiceTest {


  @Autowired
  InvoiceRepository invoiceRepository;

  private void setUp() {
    Invoice invoice = new Invoice();
    Company company = new Company();
    company.setName("Blue");
    company.setVatIdentificationNumber("PL23456");
    invoice.setBuyer(company);
    List<InvoiceItem> items = new ArrayList<>();
    InvoiceItem element = new InvoiceItem();
    element.setVat(Vat.VAT_23);
    element.setValue(BigDecimal.valueOf(12));
    element.setQuantity(1);
    element.setDescription("Phone");
    items.add(element);
    invoice.setItems(items);
    invoice.setLocalDate(LocalDate.now());
    Company company2 = new Company();
    company2.setName("Yellow");
    company2.setVatIdentificationNumber("PL11123456");
    invoice.setSeller(company2);
    invoice.setId(1);
    invoiceRepository.save(invoice);
  }

  @Test
  public void shouldGetInvoices() throws Exception {
    InvoiceService invoiceService = new InvoiceService(invoiceRepository);
    setUp();
    assertNotNull(invoiceService.getInvoices());
  }

  @Test
  public void shouldGetOneInvoice() throws Exception {
    InvoiceService invoiceService = new InvoiceService(invoiceRepository);
    setUp();
    assertNotNull(invoiceService.getOneInvoice(1));
  }

  @Test
  public void shouldDeleteInvoice() throws Exception {
    InvoiceService invoiceService = new InvoiceService(invoiceRepository);
    setUp();
    invoiceService.deleteInvoice(1);
    Assert.assertNull(invoiceService.getOneInvoice(1));
  }

  @Test
  public void shouldInsertInvoice() throws Exception {
    InvoiceService invoiceService = new InvoiceService(invoiceRepository);
    Invoice invoice = new Invoice();
    Company company = new Company();
    company.setName("Green");
    company.setVatIdentificationNumber("PL1223456");
    invoice.setBuyer(company);
    List<InvoiceItem> items = new ArrayList<>();
    InvoiceItem element = new InvoiceItem();
    element.setVat(Vat.VAT_23);
    element.setValue(BigDecimal.valueOf(12));
    element.setQuantity(1);
    element.setDescription("TV");
    items.add(element);
    invoice.setItems(items);
    invoice.setLocalDate(LocalDate.now());
    Company company2 = new Company();
    company2.setName("Black");
    company2.setVatIdentificationNumber("PL111023456");
    invoice.setSeller(company2);
    invoice.setId(1);
    invoiceService.insertInvoice(company, company2, LocalDate.now(), items);
    Assert.assertTrue(invoiceService.getOneInvoice(1).getSeller().getName() == "Green");
  }

  @Test
  public void shouldUpdateInvoice() throws Exception {
    InvoiceService invoiceService = new InvoiceService(invoiceRepository);
    setUp();
    Company company = new Company();
    company.setVatIdentificationNumber("PL1234567");
    company.setName("Pink");
    Company company2 = new Company();
    company2.setName("Violet");
    company2.setVatIdentificationNumber("PL0987565");
    invoiceService.updateInvoice(1, company, company2, LocalDate.now(), null);
    assertTrue(invoiceService.getOneInvoice(1).getSeller().getName() == "Pink");
  }

}