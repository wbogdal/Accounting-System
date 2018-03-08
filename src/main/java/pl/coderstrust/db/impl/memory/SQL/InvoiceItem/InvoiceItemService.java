package pl.coderstrust.db.impl.memory.SQL.InvoiceItem;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;
import pl.coderstrust.model.InvoiceItem;
import pl.coderstrust.model.Vat;

@Repository
public class InvoiceItemService {

  @Resource
  private InvoiceItemRepository invoiceItemRepository;

  public InvoiceItemService(InvoiceItemRepository invoiceItemRepository) {
    this.invoiceItemRepository = invoiceItemRepository;
  }

  public List<InvoiceItem> getInvoiceItems() {
    return invoiceItemRepository.findAll();
  }

  public InvoiceItem getInvoiceItemById(long id) {
    return invoiceItemRepository.findOne(id);
  }

  public void deleteInvoiceItemById(long id) {
    invoiceItemRepository.deleteInvoiceItemById(id);
  }

  public InvoiceItem getInvoiceItemByDescription(String description) {
    return invoiceItemRepository.getInvoiceItemByDescription(description);
  }

  public void deleteInvoiceItemByDescription(String description) {
    invoiceItemRepository.deleteInvoiceItemByDescription(description);
  }

  public void insertInvoiceItem(String description, int quantity, BigDecimal value, Vat vat) {
    InvoiceItem invoiceItem = new InvoiceItem();
    invoiceItem.setDescription(description);
    invoiceItem.setQuantity(quantity);
    invoiceItem.setValue(value);
    invoiceItem.setVat(vat);
    invoiceItemRepository.save(invoiceItem);
  }

  public void updateInvoiceItem(Long id, String description, int quantity, BigDecimal value,
      Vat vat) {
    Iterator<InvoiceItem> invoiceItemIterator = invoiceItemRepository.findAll().iterator();
    while (invoiceItemIterator.hasNext()) {
      InvoiceItem current = invoiceItemIterator.next();
      if (current.getId() == id) {
        current.setDescription(description);
        current.setQuantity(quantity);
        current.setValue(value);
        current.setVat(vat);
        invoiceItemRepository.save(current);
      }
    }
  }

}
