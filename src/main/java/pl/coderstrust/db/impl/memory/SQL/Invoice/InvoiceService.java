package pl.coderstrust.db.impl.memory.SQL.Invoice;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceItem;

@Repository
public class InvoiceService {

  @Resource
  private InvoiceRepository invoiceRepository;

  public InvoiceService(InvoiceRepository invoiceRepository) {
    this.invoiceRepository = invoiceRepository;
  }

  public List<Invoice> getInvoices() {
    return invoiceRepository.findAll();
  }

  public Invoice getOneInvoice(Integer id) {
    return invoiceRepository.findOne(id);
  }

  public boolean deleteInvoice(Integer id) {
    invoiceRepository.delete(id);
    return invoiceRepository.findOne(id) == null;
  }

  public void insertInvoice(Company seller, Company buyer, LocalDate date,
      List<InvoiceItem> items) {
    Invoice invoice = new Invoice();
    invoice.setSeller(seller);
    invoice.setLocalDate(date);
    invoice.setItems(items);
    invoice.setBuyer(buyer);
    invoiceRepository.save(invoice);
  }

  public void updateInvoice(Integer id, Company seller, Company buyer, LocalDate date,
      List<InvoiceItem> items) {
    Iterator<Invoice> invoiceIterator = invoiceRepository.findAll().iterator();
    while (invoiceIterator.hasNext()) {
      Invoice current = invoiceIterator.next();
      if (current.getId() == id) {
        current.setSeller(seller);
        current.setLocalDate(date);
        current.setItems(items);
        current.setBuyer(buyer);
        invoiceRepository.save(current);
      }
    }
  }
}
