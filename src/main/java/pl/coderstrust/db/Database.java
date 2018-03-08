package pl.coderstrust.db;

import java.util.List;
import pl.coderstrust.model.Invoice;

public interface Database {

  void saveInvoice(Invoice invoice);

  void removeInvoice(int id);

  int getLastId();

  List<Invoice> getInvoices();

  List<Invoice> getInvoicesFromCurrentDay();

}
