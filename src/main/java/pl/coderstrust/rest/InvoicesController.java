package pl.coderstrust.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.logic.InvoiceBook;
import pl.coderstrust.model.Invoice;

import java.time.LocalDate;
import java.util.List;

@RestController
public class InvoicesController {

  @Autowired
  public InvoicesController(InvoiceBook invoiceBook) {
    this.invoiceBook = invoiceBook;
  }
//  ObjectMapperProvider objectMapperProvider= new ObjectMapperProvider();
//  JsonConverter jsonConverter = new JsonConverter(objectMapperProvider.mapper());
//pamietac o tym zeby wywali tutaj na sztywno baze mongo///
  private InvoiceBook invoiceBook;// = new InvoiceBook(new MongoDatabase(jsonConverter));

  @RequestMapping(path = "/invoices/{id}", method = RequestMethod.GET)
  public Invoice getInvoice(@PathVariable int id) {
    return invoiceBook.getInvoiceById(id);
  }

  @RequestMapping(path = "/invoices", method = RequestMethod.POST)
  public ResponseEntity<Object> createInvoice(@RequestBody Invoice invoice) {
    invoiceBook.addInvoice(invoice);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @RequestMapping(path = "/invoices/{id}", method = RequestMethod.PUT)
  public void updateInvoice(@PathVariable int id, @RequestBody Invoice invoice) {
    invoiceBook.getInvoiceById(id).setLocalDate(LocalDate.now());
    invoiceBook.getInvoiceById(id).setSeller(invoice.getSeller());
    invoiceBook.getInvoiceById(id).setBuyer(invoice.getBuyer());
    invoiceBook.getInvoiceById(id).setItems(invoice.getItems());
  }

  @RequestMapping(path = "/invoices/{id}", method = RequestMethod.DELETE)
  public void deleteInvoice(@PathVariable int id) {
    invoiceBook.removeInvoice(getInvoice(id));
  }

  @RequestMapping(path = "/invoices/byDate/{localDate}", method = RequestMethod.GET)
  public List<Invoice> filter(@PathVariable List<Invoice> invoices, LocalDate startDate,
      LocalDate endDate) {
    return invoiceBook.filter(invoices, startDate, endDate);
  }
}
