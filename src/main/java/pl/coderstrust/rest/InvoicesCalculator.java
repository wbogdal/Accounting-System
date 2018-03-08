package pl.coderstrust.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.logic.CreatePDFProvider;
import pl.coderstrust.logic.InvoiceBook;
import pl.coderstrust.logic.calculator.IncomeSumCalculator;
import pl.coderstrust.logic.calculator.OverpaidVatCalculator;
import pl.coderstrust.logic.calculator.PaidSumCalculator;
import pl.coderstrust.logic.calculator.VatToPayCalculator;
import pl.coderstrust.model.Invoice;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class InvoicesCalculator {

  private final IncomeSumCalculator incomeSumCalculator;
  private final PaidSumCalculator paidSumCalculator;
  private final VatToPayCalculator vatToPayCalculator;
  private final OverpaidVatCalculator overpaidVatCalculator;
  private final CreatePDFProvider createPDFProvider;
  private final InvoiceBook invoiceBook; //pamietac o tym zeby wywali tutaj na sztywno baze mongo///


  @Autowired
  public InvoicesCalculator(InvoiceBook invoiceBook, IncomeSumCalculator incomeSumCalculator,
      PaidSumCalculator paidSumCalculator, VatToPayCalculator vatToPayCalculator,
      OverpaidVatCalculator overpaidVatCalculator, CreatePDFProvider createPDFProvider) {
    this.invoiceBook = invoiceBook;
    this.incomeSumCalculator = incomeSumCalculator;
    this.paidSumCalculator = paidSumCalculator;
    this.overpaidVatCalculator = overpaidVatCalculator;
    this.vatToPayCalculator = vatToPayCalculator;
    this.createPDFProvider=createPDFProvider;
  }

  @RequestMapping(path = "/invoices", method = RequestMethod.GET)
  public List<Invoice> getInvoices() {
    return invoiceBook.getInvoices();
  }

  @RequestMapping(path = "/createPDF/{id}", method = RequestMethod.GET)
  public void generatePDF(@PathVariable int id) throws IOException {
    Invoice invoice=invoiceBook.getInvoiceById(id);
    String dest = "src/main/resources/pdf/Invoice"+ invoice.getId()+".pdf";
    createPDFProvider.createPdf(invoice,dest);
  }

  @RequestMapping(path = "/invoices/sum/{month}/{year}", method = RequestMethod.GET)
  public BigDecimal calculateSum(@PathVariable int month, @PathVariable int year) {

    return invoiceBook.accept(incomeSumCalculator, invoiceBook.getInvoices(), month, month,
        year);
  }

  @RequestMapping(path = "/invoices/paidsum/{month}/{year}", method = RequestMethod.GET)
  public BigDecimal calculatePaidSum(@PathVariable int month, @PathVariable int year) {

    return invoiceBook.accept(paidSumCalculator, invoiceBook.getInvoices(), month, month,
        year);

  }

  @RequestMapping(path = "/invoices/vattopay",
      params = {"month", "year"},
      method = RequestMethod.GET)
  public BigDecimal calculateVatToPay(@RequestParam(name = "month") int month,
      @RequestParam int year) {

    return invoiceBook.accept(vatToPayCalculator, invoiceBook.getInvoices(), month, month,
        year);

  }

  @RequestMapping(path = "/invoices/overpaidvat",
      params = {"month", "year"},
      method = RequestMethod.GET)
  public BigDecimal calculateOverpaidVat(@RequestParam(name = "month") int month,
      @RequestParam int year) {

    return invoiceBook.accept(overpaidVatCalculator, invoiceBook.getInvoices(), month, month,
        year);
  }

  @RequestMapping(path = "/invoices/vatdifference",
      params = {"month", "year"},
      method = RequestMethod.GET)
  public BigDecimal vatOverpaidAndToPayDifference(@RequestParam(name = "month") int month,
      @RequestParam int year) {
    return invoiceBook.accept(vatToPayCalculator, invoiceBook.getInvoices(), month, month,
        year)
        .subtract(invoiceBook.accept(overpaidVatCalculator, invoiceBook.getInvoices(), month, month,
            year));
  }

  //method counting incomeTax for one month
  @RequestMapping(path = "/invoices/monthlyincometax",
      params = {"month", "year", "healthInsuranceAmount", "socialInsuranceAmount", "taxFreeAmount"},
      method = RequestMethod.GET)
  public BigDecimal calculateIncomeTax(@RequestParam(name = "month") int month,
      @RequestParam int year,
      @RequestParam double healthInsuranceAmount,
      @RequestParam double socialInsuranceAmount,
      @RequestParam double taxFreeAmount) {

    BigDecimal revenue;
    BigDecimal tax;

    BigDecimal healthInsurance = BigDecimal.valueOf(healthInsuranceAmount);
    BigDecimal socialInsurance = BigDecimal.valueOf(socialInsuranceAmount);
    BigDecimal taxFree = BigDecimal.valueOf(taxFreeAmount);

    revenue = invoiceBook.accept(incomeSumCalculator, invoiceBook.getInvoices(), month, month,
        year)
        .subtract(invoiceBook.accept(paidSumCalculator, invoiceBook.getInvoices(), month, month,
            year));
    tax = revenue.subtract(socialInsurance);
    tax = tax.setScale(0, BigDecimal.ROUND_HALF_UP);
    tax = tax.multiply(BigDecimal.valueOf(0.18));
    tax = tax.subtract(healthInsurance);
    tax = tax.subtract(taxFree);
    return tax.setScale(0, BigDecimal.ROUND_HALF_UP);
  }

  //method counting incomeTax from the beginning of the year to given month
  @RequestMapping(path = "/invoices/yearlyincometax",
      params = {"month", "year", "healthInsuranceAmount", "socialInsuranceAmount", "taxFreeAmount"},
      method = RequestMethod.GET)
  public BigDecimal calculateYearlyIncomeTax(@RequestParam(name = "month") int month,
      @RequestParam int year,
      @RequestParam double healthInsuranceAmount,
      @RequestParam double socialInsuranceAmount,
      @RequestParam double taxFreeAmount) {

    BigDecimal revenue;
    BigDecimal tax;

    BigDecimal healthInsurance = BigDecimal.valueOf(healthInsuranceAmount);
    BigDecimal socialInsurance = BigDecimal.valueOf(socialInsuranceAmount);
    BigDecimal taxFree = BigDecimal.valueOf(taxFreeAmount);

    revenue = invoiceBook.accept(incomeSumCalculator, invoiceBook.getInvoices(), 1, month,
        year)
        .subtract(invoiceBook.accept(paidSumCalculator, invoiceBook.getInvoices(), 1, month,
            year));
    tax = revenue.subtract(socialInsurance);
    tax = tax.setScale(0, BigDecimal.ROUND_HALF_UP);
    tax = tax.multiply(BigDecimal.valueOf(0.18));
    tax = tax.subtract(healthInsurance);
    tax = tax.subtract(taxFree);
    return tax.setScale(0, BigDecimal.ROUND_HALF_UP);
  }
}

