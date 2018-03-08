package pl.coderstrust.db.impl.memory.SQL.InvoiceItem;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.InvoiceItem;
import pl.coderstrust.model.Vat;

import java.util.List;

@RestController
@RequestMapping(path = "/invoice")
public class InvoiceItemController {

  private InvoiceItemService invoiceItemService;

  public InvoiceItemController(InvoiceItemService invoiceItemService) {
    this.invoiceItemService = invoiceItemService;
  }

  @CrossOrigin
  @GetMapping(value = "/")
  public List<InvoiceItem> getInvoiceItems() {
    return invoiceItemService.getInvoiceItems();
  }

  @CrossOrigin
  @RequestMapping(path = "/invoiceitem/{id}", method = RequestMethod.GET)
  public InvoiceItem getInvoiceItemByIdentificationNumber(@PathVariable long id) {
    return invoiceItemService.getInvoiceItemById(id);
  }

  @CrossOrigin
  @RequestMapping(path = "/invoiceitem/delete/{id}", method = RequestMethod.DELETE)
  public void deleteInvoiceItemById(@PathVariable long id) {
    invoiceItemService.deleteInvoiceItemById(id);
  }

  @CrossOrigin
  @RequestMapping(path = "/invoiceitem/{description}", method = RequestMethod.GET)
  public InvoiceItem getInvoiceItemByDescription(@PathVariable String descripton) {
    return invoiceItemService.getInvoiceItemByDescription(descripton);
  }

  @CrossOrigin
  @RequestMapping(path = "/invoiceitem/delete/{description}", method = RequestMethod.DELETE)
  public void deleteInvoiceItemByDescription(@PathVariable String description) {
   invoiceItemService.deleteInvoiceItemByDescription(description);
  }

  @CrossOrigin
  @RequestMapping(value = "invoiceitem/update",
      params = {"id", "description", "quantity", "value", "vat", "vat"},
      method = RequestMethod.PUT)
  public void updateInvoiceItem(@RequestParam("id") long id, @RequestParam("description")
      String description, @RequestParam("quantity") int quantity, @RequestParam("value")
      java.math.BigDecimal value, @RequestParam("vat") Vat vat) {
    invoiceItemService.updateInvoiceItem(id,description,quantity,value, vat);
  }


  @CrossOrigin
  @RequestMapping(value = "invoiceitem/insert",
      params = {"id", "description", "quantity", "value", "vat", "vat"},
      method = RequestMethod.POST)
  public void insertInvoiceItem(@RequestParam("id") long id, @RequestParam("description")
      String description, @RequestParam("quantity") int quantity, @RequestParam("value")
      java.math.BigDecimal value, @RequestParam("vat") Vat vat) {
    invoiceItemService.insertInvoiceItem(description,quantity,value,vat);
  }
}
