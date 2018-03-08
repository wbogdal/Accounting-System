package pl.coderstrust.db.impl.memory;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import pl.coderstrust.db.Database;
import pl.coderstrust.model.Invoice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Primary
@Repository
public class InMemoryDatabase implements Database, IdGenerator {

  private List<Invoice> invoices = new ArrayList<>();
  private static final AtomicInteger counter = new AtomicInteger(1);


  @Override
  public void saveInvoice(Invoice invoice) {
    invoices.add(invoice);
  }

  @Override
  public void removeInvoice(int id) {
    for (int i = 0; i < invoices.size(); i++) {
      if (invoices.get(i).getId() == id) {
        invoices.remove(i);
      }
    }
  }

  @Override
  public List<Invoice> getInvoices() {
    return invoices.stream().sorted().collect(Collectors.toList());
  }

  public List<Invoice> getInvoicesFromCurrentDay() {
    return invoices.stream().filter(invoice -> invoice.getLocalDate().isEqual(LocalDate.now()))
        .collect(Collectors.toList());
  }

  public int generate() {
    return counter.getAndIncrement();
  }

  public int getLastId() {
    return getInvoices().get(invoices.size()-1).getId();
  }


}


