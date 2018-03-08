package pl.coderstrust.logic;

import java.time.LocalDate;
import java.util.List;
import pl.coderstrust.db.impl.memory.IdGenerator;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceItem;

public class InvoiceFactory {

  private final IdGenerator idGenerator;

  public InvoiceFactory(IdGenerator idGenerator) {
    this.idGenerator = idGenerator;
  }

  public Invoice create(LocalDate localDate, Company seller, Company buyer,
      List<InvoiceItem> items) {
    int id = idGenerator.generate();
    return new Invoice(id, localDate, seller, buyer, items);
  }
}
