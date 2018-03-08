package pl.coderstrust.db.impl.memory.SQL.InvoiceItem;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import pl.coderstrust.model.InvoiceItem;
import pl.coderstrust.model.Vat;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
public class InvoiceItemServiceTest {

  @Autowired
  InvoiceItemRepository invoiceItemRepository;

  @Test
  public void shouldGetInvoiceItems() throws Exception {
    //given
    InvoiceItemService invoiceItemService = new InvoiceItemService(invoiceItemRepository);
    InvoiceItem invoiceItem = new InvoiceItem();
    invoiceItem.setDescription("Phone");
    invoiceItem.setQuantity(2);
    invoiceItem.setValue(BigDecimal.valueOf(12));
    invoiceItem.setVat(Vat.VAT_23);

    //when
    invoiceItemRepository.save(invoiceItem);

    //then
    assertTrue(invoiceItemService.getInvoiceItems() != null);

  }

  @Test
  public void shouldGetInvoiceItemById() throws Exception {
    //given
    InvoiceItemService invoiceItemService = new InvoiceItemService(invoiceItemRepository);
    InvoiceItem invoiceItem = new InvoiceItem();
    invoiceItem.setDescription("Phone");
    invoiceItem.setQuantity(2);
    invoiceItem.setValue(BigDecimal.valueOf(12));
    invoiceItem.setVat(Vat.VAT_23);

    //when
    invoiceItemRepository.save(invoiceItem);

    //then
    Assert.assertEquals(invoiceItemService.getInvoiceItemById(invoiceItem.getId()),
        invoiceItemRepository.findOne(invoiceItem.getId()));
  }

  @Test
  public void shouldDeleteInvoiceItemById() throws Exception {
    //given
    InvoiceItemService invoiceItemService = new InvoiceItemService(invoiceItemRepository);
    InvoiceItem invoiceItem = new InvoiceItem();
    invoiceItem.setDescription("Phone");
    invoiceItem.setQuantity(2);
    invoiceItem.setValue(BigDecimal.valueOf(12));
    invoiceItem.setVat(Vat.VAT_23);

    //when
    invoiceItemService.deleteInvoiceItemById(invoiceItem.getId());

    //then
    Assert.assertNull(invoiceItemRepository.getInvoiceItemById(invoiceItem.getId()));
  }

  @Test
  public void shouldGetInvoiceItemByDescription() throws Exception {
    //given
    InvoiceItemService invoiceItemService = new InvoiceItemService(invoiceItemRepository);
    InvoiceItem invoiceItem = new InvoiceItem();
    invoiceItem.setDescription("TV");
    invoiceItem.setQuantity(2);
    invoiceItem.setValue(BigDecimal.valueOf(12));
    invoiceItem.setVat(Vat.VAT_23);

    //when
    invoiceItemRepository.save(invoiceItem);

    //then
    Assert.assertNotNull(invoiceItemService.getInvoiceItemByDescription("TV"));
  }

  @Test
  public void shouldDeleteInvoiceItemByDescription() throws Exception {
    //given
    InvoiceItemService invoiceItemService = new InvoiceItemService(invoiceItemRepository);
    InvoiceItem invoiceItem = new InvoiceItem();
    invoiceItem.setDescription("Computer");
    invoiceItem.setQuantity(2);
    invoiceItem.setValue(BigDecimal.valueOf(12));
    invoiceItem.setVat(Vat.VAT_23);

    //when
    invoiceItemService.deleteInvoiceItemByDescription("Computer");

    //then
    Assert.assertNull(invoiceItemRepository.getInvoiceItemByDescription("Computer"));
  }


  @Test
  public void insertInvoiceItem() throws Exception {
    //given
    InvoiceItemService invoiceItemService = new InvoiceItemService(invoiceItemRepository);

    String description = "Beer";
    int quantity = 3;
    BigDecimal value = (BigDecimal.valueOf(12));
    Vat vat = Vat.VAT_23;

    //when
    invoiceItemService.insertInvoiceItem(description, quantity, value, vat);

    //then
    Assert.assertNotNull(invoiceItemRepository.getInvoiceItemByDescription("Beer"));

  }

  @Test
  public void shouldUpdateInvoiceItem() throws Exception {
    //given
    InvoiceItemService invoiceItemService = new InvoiceItemService(invoiceItemRepository);
    InvoiceItem invoiceItem = new InvoiceItem();
    invoiceItem.setId(1);
    invoiceItem.setDescription("Phone");
    invoiceItem.setQuantity(2);
    invoiceItem.setValue(BigDecimal.valueOf(12));
    invoiceItem.setVat(Vat.VAT_23);
    invoiceItemRepository.save(invoiceItem);

    //when
    invoiceItemService.updateInvoiceItem(invoiceItem.getId(), "Phone",
        4, BigDecimal.valueOf(12), Vat.VAT_23);

    //then
    Assert.assertTrue(invoiceItemRepository.getInvoiceItemById(invoiceItem.getId()).
        getQuantity() == 4);
  }
}