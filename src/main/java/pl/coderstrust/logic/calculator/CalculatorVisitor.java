package pl.coderstrust.logic.calculator;

import java.math.BigDecimal;
import pl.coderstrust.model.Invoice;

public interface CalculatorVisitor {

  public BigDecimal visit(Invoice invoice);
}
