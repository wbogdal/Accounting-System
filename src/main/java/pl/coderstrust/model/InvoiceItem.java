package pl.coderstrust.model;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INVOICE_ITEM")
public class InvoiceItem {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, unique = true)
  private long id;

  private String description;

  private int quantity;

  @Column(name = "value", nullable = false)
  private BigDecimal value;

  private Vat vat;

  public InvoiceItem() {
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public InvoiceItem(String description, BigDecimal value, Vat vat) {
    this.description = description;
    this.value = value;
    this.vat = vat;
  }

  public long getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }

  public void setVat(Vat vat) {
    this.vat = vat;
  }

  @Column(name = "description", nullable = false)
  public String getDescription() {
    return description;
  }

  public BigDecimal getValue() {
    return value;
  }

  @Enumerated(EnumType.STRING)
  @Column(name = "vat", nullable = false)
  public Vat getVat() {
    return vat;
  }


  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    InvoiceItem that = (InvoiceItem) object;
    return Objects.equals(description, that.description)
        &&
        Objects.equals(value, that.value)
        &&
        vat == that.vat;
  }

  @Override
  public int hashCode() {

    return Objects.hash(description, value, vat);
  }

  @Column(name = "quantity", nullable = false)
  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public String toString() {
    return id + description + quantity + value + vat;
  }
}

