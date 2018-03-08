package pl.coderstrust.logic.calculator;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceItem;

@Component
public class VatToPayCalculator implements CalculatorVisitor {

  @Override
  public BigDecimal visit(Invoice invoice) {
    BigDecimal sum = BigDecimal.ZERO;
    if (invoice.getSeller().getName().equals("CodersTrust")) {
      for (InvoiceItem current : invoice.getItems()) {
        sum = sum.add(current.getValue().multiply(current.getVat().getVat()) == null ?
            BigDecimal.ZERO : current.getValue().multiply(current.getVat().getVat()));
      }
    }
    sum = sum.setScale(2, BigDecimal.ROUND_HALF_UP);
    return sum;
  }
}
