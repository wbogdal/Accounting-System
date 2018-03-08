package pl.coderstrust.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "COMPANY")
public class Company {

  @Id
  @Column(name = "vat_identification_number", nullable = false, unique = true)
  private String vatIdentificationNumber;

  @Column(name = "name", nullable = false)
  private String name;


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getVatIdentificationNumber() {
    return vatIdentificationNumber;
  }

  public void setVatIdentificationNumber(String vatIdentificationNumber) {
    this.vatIdentificationNumber = vatIdentificationNumber;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (!(object instanceof Company)) {
      return false;
    }

    Company company = (Company) object;

    if (getName() != null ? !getName().equals(company.getName()) : company.getName() != null) {
      return false;
    }
    return vatIdentificationNumber != null ? vatIdentificationNumber
        .equals(company.vatIdentificationNumber) : company.vatIdentificationNumber == null;
  }

  @Override
  public int hashCode() {
    int result = getName() != null ? getName().hashCode() : 0;
    result =
        31 * result + (vatIdentificationNumber != null ? vatIdentificationNumber.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Company{"
        + "name='" + name + '\''
        + ", vatIdentificationNumber='" + vatIdentificationNumber + '\''
        + '}';
  }


}
