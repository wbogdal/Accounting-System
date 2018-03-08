package pl.coderstrust.logic;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.springframework.stereotype.Service;
import pl.coderstrust.db.Database;
import pl.coderstrust.logic.calculator.CalculatorVisitor;
import pl.coderstrust.mail.EmailServiceImpl;
import pl.coderstrust.model.Invoice;


@Service
public class InvoiceBook {

  private Database dataBase;
  private EmailServiceImpl emailService;


  public InvoiceBook(Database dataBase, EmailServiceImpl emailService) {
    this.dataBase = dataBase;
    this.emailService = emailService;
  }

  public void addInvoice(Invoice invoice) {
    dataBase.saveInvoice(invoice);
    emailService.sendEmail("hawlik1989@gmail.com", "hawlik1989@gmail.com",
        "hawlik1989@gmail.com", invoice);
  }

  public void removeInvoice(Invoice invoice) {
    dataBase.removeInvoice(invoice.getId());
  }

  public Invoice getInvoiceById(int id) {
    List<Invoice> invocies = dataBase.getInvoices();
    for (int i = 0; i < invocies.size(); i++) {
      if (invocies.get(i).getId() == id) {
        return invocies.get(i);
      }
    }
    return null;
  }

  boolean isFromTimePeriod(LocalDate localDate, LocalDate startDate, LocalDate endDate) {
    return !(localDate.isBefore(startDate) || localDate.isAfter(endDate));
  }

  public List<Invoice> filter(List<Invoice> invoices, LocalDate startDate, LocalDate endDate) {
    List<Invoice> filterInvoiceList = new ArrayList<Invoice>();
    for (int i = 0; i < invoices.size(); i++) {
      if (isFromTimePeriod(invoices.get(i).getLocalDate(), startDate, endDate)) {
        filterInvoiceList.add(invoices.get(i));
      }
    }
    return filterInvoiceList;
  }

  private static int numberOfDaysInMonth(int month, int year) {
    Calendar actualMonth = new GregorianCalendar(year, month - 1, 1);
    return actualMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
  }

  public BigDecimal accept(CalculatorVisitor visitor, List<Invoice> invoices, int startMonth,
      int endMonth, int year) {

    LocalDate startDate = LocalDate.of(year, startMonth, 1);
    LocalDate endDate = LocalDate.of(year, endMonth, numberOfDaysInMonth(endMonth, year));
    List<Invoice> actual = filter(invoices, startDate, endDate);
    BigDecimal sum = BigDecimal.ZERO;
    for (Invoice invoice : actual) {
      sum = sum.add(visitor.visit(invoice));
    }
    return sum;
  }

  public List<Invoice> getInvoices() {
    return dataBase.getInvoices();
  }
}
