package pl.coderstrust.db.impl.memory.SQL.Invoice;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceItem;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/invoicecontroller")
public class InvoiceController {

  private InvoiceService invoiceService;

  public InvoiceController(InvoiceService invoiceService) {
    this.invoiceService = invoiceService;
  }

  @CrossOrigin
  @GetMapping(value = "/")
  public List<Invoice> getInvoices() {
    return invoiceService.getInvoices();
  }

  @CrossOrigin
  @GetMapping(value = "/{id}")
  public Invoice getOneInvoice(@PathVariable Integer id) {
    return invoiceService.getOneInvoice(id);
  }

  @CrossOrigin
  @DeleteMapping(value = "/delete/{id}")
  public void deleteInvoice(@PathVariable Integer id){
    invoiceService.deleteInvoice(id);
  }

  @CrossOrigin
  @RequestMapping(value = "invoice/update",
      params = {"id", "seller", "buyer", "date", "items"},
      method = RequestMethod.PUT)
  public void updateInvoice(@RequestParam("id") Integer id, @RequestParam("seller")
      pl.coderstrust.model.Company seller, @RequestParam("buyer") Company buyer,
      @RequestParam("date") LocalDate date, @RequestParam("Items") List<InvoiceItem> items) {
    invoiceService.updateInvoice(id,seller,buyer,date,items);
  }


  @CrossOrigin
  @RequestMapping(value = "invoice/insert",
      params = {"seller", "buyer", "date", "items"},
      method = RequestMethod.POST)
  public void insertInvoice(@RequestParam("seller")
      pl.coderstrust.model.Company seller, @RequestParam("buyer") Company buyer,
      @RequestParam("date") LocalDate date, @RequestParam("Items") List<InvoiceItem> items) {
    invoiceService.insertInvoice(seller,buyer,date,items);
  }
}
