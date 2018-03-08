package pl.coderstrust.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Component
@Entity
@Table(name = "INVOICE")

public class Invoice implements Comparable<Invoice> {

  private int id;
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private LocalDate localDate;
  private Company seller;
  private Company buyer;
  List<InvoiceItem> items;

  public Invoice() {
  }

  public Invoice(int id, LocalDate localDate, Company seller, Company buyer,
      List<InvoiceItem> items) {
    this.id = id;
    this.localDate = localDate;
    this.seller = seller;
    this.buyer = buyer;
    this.items = items;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, unique = true)
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Column(name = "local_date", nullable = false)
  public LocalDate getLocalDate() {
    return localDate;
  }

  public void setLocalDate(LocalDate localDate) {
    this.localDate = localDate;
  }

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "vat_identification_number_1")
  public Company getSeller() {
    return seller;
  }

  public void setSeller(Company seller) {
    this.seller = seller;
  }

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "vat_identification_number_2")
  public Company getBuyer() {
    return buyer;
  }

  public void setBuyer(Company buyer) {
    this.buyer = buyer;
  }

  @ManyToMany(cascade = {CascadeType.ALL})
  @JoinTable(
      name = "invoice_invoice_item",
      joinColumns = {@JoinColumn(name = "invoice_id")},
      inverseJoinColumns = {@JoinColumn(name = "item_id")}
  )
  public List<InvoiceItem> getItems() {
    return items;
  }

  public void setItems(List<InvoiceItem> items) {
    this.items = items;
  }

  @Override
  public int compareTo(Invoice invoice) {
    if (invoice == null) {
      return -1;
    }

    if (this == null) {

      return -1;
    }

    int comparison = (this.localDate.getYear() - invoice.localDate.getYear());
    if (comparison == 0) {
      comparison = (this.localDate.getMonthValue() - invoice.localDate.getMonthValue());
      if (comparison == 0) {
        comparison = (this.localDate.getDayOfMonth() - invoice.localDate.getDayOfMonth());
      }
    }
    if (comparison < 0) {
      return -1;
    }
    if (comparison > 0) {
      return 1;
    }
    return 0;
  }


  public String toString() {
    return "id number: " + id + " " + localDate + " " + seller + " " + buyer + " " + items;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Invoice invoice = (Invoice) object;
    return id == invoice.id
        &&
        Objects.equals(localDate, invoice.localDate)
        &&
        Objects.equals(seller, invoice.seller)
        &&
        Objects.equals(buyer, invoice.buyer)
        &&
        items.equals(invoice.items);
  }

  @Override
  public int hashCode() {

    return Objects.hash(id, localDate, seller, buyer, items);
  }
}
