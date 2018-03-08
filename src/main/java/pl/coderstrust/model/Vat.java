package pl.coderstrust.model;

import java.math.BigDecimal;

public enum Vat {

  VAT_ZW(new BigDecimal(0)),
  VAT_7(new BigDecimal(0.07)),
  VAT_23(new BigDecimal(0.23));

  private BigDecimal value;

  private Vat(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getVat() {
    return value;
  }
}
