package pl.coderstrust.db.impl.memory.SQL.Invoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderstrust.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

  Invoice getInvoiceById(Integer id);

}
