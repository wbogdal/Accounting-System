package pl.coderstrust.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import pl.coderstrust.db.Database;
import pl.coderstrust.db.impl.memory.InMemoryDatabase;
import pl.coderstrust.logic.calculator.CalculatorVisitor;
import pl.coderstrust.logic.calculator.IncomeSumCalculator;
import pl.coderstrust.logic.calculator.OverpaidVatCalculator;
import pl.coderstrust.logic.calculator.PaidSumCalculator;
import pl.coderstrust.logic.calculator.VatToPayCalculator;
import pl.coderstrust.mail.EmailServiceImpl;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceItem;
import pl.coderstrust.model.Vat;

public class InvoiceBookTest {

  InMemoryDatabase database = new InMemoryDatabase();
  InvoiceFactory invoiceFactory = new InvoiceFactory(database);

  final Database mock = mock(Database.class);
  final EmailServiceImpl emailServiceMock = mock(EmailServiceImpl.class);

  @Test
  public void shouldFromTimePeriodTest() {

    //given

    LocalDate startDate = LocalDate.of(2017, 8, 10);
    LocalDate endDate = LocalDate.of(2017, 10, 10);

    //when

    when(mock.getInvoices()).thenReturn(newInvoices());
    boolean actual = new InvoiceBook(mock, emailServiceMock)
        .isFromTimePeriod(mock.getInvoices().get(0).getLocalDate(), startDate,
            endDate);

    boolean expected = true;

    //then
    assertEquals(expected, actual);
  }

  public List<Invoice> newInvoices() {
    InvoiceFactory invoiceFactory = new InvoiceFactory(new InMemoryDatabase() {
      private int next = 0;

      @Override
      public int generate() {
        return ++next;
      }
    });

    ArrayList<Invoice> invoices = new ArrayList<>();

    invoices.add(invoiceFactory.create(LocalDate.of(2017, 8, 10), new Company(),
        new Company(), new ArrayList<InvoiceItem>()));
    invoices.add(invoiceFactory.create(LocalDate.of(2017, 9, 5), new Company(),
        new Company(), new ArrayList<InvoiceItem>()));
    invoices.add(invoiceFactory.create(LocalDate.of(2017, 9, 10), new Company(),
        new Company(), new ArrayList<InvoiceItem>()));
    invoices.add(invoiceFactory.create(LocalDate.of(2017, 8, 12), new Company(),
        new Company(), new ArrayList<InvoiceItem>()));
    return invoices;

  }

  @Test
  public void shouldFilterTest() {
    final Database mock = mock(Database.class);

    //given

    LocalDate startDate = LocalDate.of(2017, 9, 1);
    LocalDate endDate = LocalDate.of(2017, 9, 12);

    List<Invoice> expected = new ArrayList<>();
    expected.add(newInvoices().get(1));
    expected.add(newInvoices().get(2));

    //when
    when(mock.getInvoices()).thenReturn(newInvoices());
    List<Invoice> given = new InvoiceBook(mock, emailServiceMock)
        .filter(mock.getInvoices(), startDate, endDate);

    //then
    assertEquals(expected.get(0).getLocalDate(), given.get(0).getLocalDate());
    assertEquals(expected.get(0).getId(), given.get(0).getId());
    assertEquals(expected.get(1).getLocalDate(), given.get(1).getLocalDate());
    assertEquals(expected.get(1).getId(), given.get(1).getId());
  }

  @Test
  public void shouldNotFilterEmptyList() {

    //given
    ArrayList<Invoice> invoices = new ArrayList<>();

    invoices.add(invoiceFactory.create(LocalDate.of(2017, 8, 10), new Company(),
        new Company(), new ArrayList<InvoiceItem>()));
    invoices.add(invoiceFactory.create(LocalDate.of(2017, 9, 5), new Company(),
        new Company(), new ArrayList<InvoiceItem>()));
    invoices.add(invoiceFactory.create(LocalDate.of(2017, 9, 10), new Company(),
        new Company(), new ArrayList<InvoiceItem>()));
    invoices.add(invoiceFactory.create(LocalDate.of(2017, 8, 12), new Company(),
        new Company(), new ArrayList<InvoiceItem>()));

    LocalDate startDate = LocalDate.of(2016, 9, 1);
    LocalDate endDate = LocalDate.of(2016, 9, 12);
    List<Invoice> filterInvoiceList = new ArrayList<>();

    //when
    when(mock.getInvoices()).thenReturn(newInvoices());
    List<Invoice> actual = new InvoiceBook(mock, emailServiceMock)
        .filter(mock.getInvoices(), startDate, endDate);

    //then
    assertEquals(filterInvoiceList, actual);
  }


  @Test
  public void shouldSaveInvoiceTest() throws Exception {

    //given
    final Database database = mock(Database.class);
    final InvoiceBook invoiceBook = new InvoiceBook(database, emailServiceMock);
    final Invoice invoice = mock(Invoice.class); // ze moge stworzyc fv
    //when
    invoiceBook.addInvoice(invoice);

    //then
    verify(database).saveInvoice(invoice);

  }


  @Test
  public void shouldRemoveInvoice() {

    //given
    final Database database = mock(Database.class);
    final InvoiceBook invoiceBook = new InvoiceBook(database, emailServiceMock);
    final Invoice invoice = mock(Invoice.class);
    //when
    invoiceBook.removeInvoice(invoice);
    //then
    verify(database).removeInvoice(invoice.getId());
  }

  @Test
  public void shouldCountIncomeSum() throws Exception {
    //given
    InvoiceBook invoiceBook = new InvoiceBook(database, emailServiceMock);
    List<Invoice> invoices = addInvoices();
    BigDecimal expected = BigDecimal.valueOf(146.50).setScale(2,
        BigDecimal.ROUND_HALF_UP);
    CalculatorVisitor sumCalculator = new IncomeSumCalculator();

    //when
    when(mock.getInvoices()).thenReturn(addInvoices());
    BigDecimal given = invoiceBook.accept(sumCalculator, invoices, 1, 1,
        2018);

    //then
    assertEquals(expected, given);
  }

  @Test
  public void shouldCountPaidSum() throws Exception {
    InvoiceBook invoiceBook = new InvoiceBook(database, emailServiceMock);
    List<Invoice> invoices = addInvoices();
    BigDecimal expected = BigDecimal.valueOf(123412.50).setScale(2,
        BigDecimal.ROUND_HALF_UP);
    CalculatorVisitor paidSumCalculator = new PaidSumCalculator();

    //when
    BigDecimal given = invoiceBook.accept(paidSumCalculator, invoices, 1, 1,
        2018);

    //then
    assertEquals(expected, given);
  }

  @Test
  public void shouldCountVatToPay() throws Exception {
    InvoiceBook invoiceBook = new InvoiceBook(database, emailServiceMock);
    List<Invoice> invoices = addInvoices();
    BigDecimal expected = BigDecimal.valueOf(4.06).setScale(2,
        BigDecimal.ROUND_HALF_UP);
    CalculatorVisitor vatToPay = new VatToPayCalculator();

    //when
    BigDecimal given = invoiceBook.accept(vatToPay, invoices, 1, 1,
        2018);

    //then
    assertEquals(expected, given);
  }

  @Test
  public void shouldCountOverpaidVat() throws Exception {
    InvoiceBook invoiceBook = new InvoiceBook(database, emailServiceMock);
    List<Invoice> invoices = addInvoices();
    BigDecimal expected = BigDecimal.valueOf(28384.88).setScale(2,
        BigDecimal.ROUND_HALF_UP);
    CalculatorVisitor overpaidVat = new OverpaidVatCalculator();

    //when
    BigDecimal given = invoiceBook.accept(overpaidVat, invoices, 1, 1,
        2018);

    //then
    assertEquals(expected, given);
  }

  public List<Invoice> addInvoices() {

    List<Invoice> invoices = new ArrayList();
    List<InvoiceItem> elements = new ArrayList();
    List<InvoiceItem> elements2 = new ArrayList();
    Company company1 = new Company();
    company1.setName("CodersTrust");
    company1.setVatIdentificationNumber("12345");

    InvoiceItem items = new InvoiceItem("Computer",
        BigDecimal.valueOf(112.50), Vat.VAT_ZW);
    elements.add(items);
    InvoiceItem items1 = new InvoiceItem("Mobile phone",
        BigDecimal.valueOf(23.50), Vat.VAT_7);
    elements.add(items1);
    InvoiceItem items2 = new InvoiceItem("Paper",
        BigDecimal.valueOf(10.50), Vat.VAT_23);
    elements.add(items2);

    Company company2 = new Company();
    company2.setName("MON");
    company2.setVatIdentificationNumber("23456");
    InvoiceItem items3 = new InvoiceItem("Tank",
        BigDecimal.valueOf(123412.50), Vat.VAT_23);
    elements2.add(items3);

    Invoice invoice1 = invoiceFactory
        .create(LocalDate.parse("2018-01-10"), company1, company2, elements);
    invoices.add(invoice1);
    Invoice invoice2 = invoiceFactory
        .create(LocalDate.parse("2018-01-10"), company2, company1, elements2);
    invoices.add(invoice2);

    return invoices;
  }


  @Test
  public void t1shouldGetInvoiceById() {

//given

    //when
    when(mock.getInvoices()).thenReturn(newInvoices());

    Invoice actual = new InvoiceBook(mock, emailServiceMock)
        .getInvoiceById(mock.getInvoices().get(3).getId());

    //then
    assertEquals(newInvoices().get(3), actual);

  }

  @Test
  public void t3shouldReturnNull() {

//given

    //when
//    Invoice actual = invoiceBook.getInvoiceById(50);
    Invoice actual;
    when(mock.getInvoices()).thenReturn(newInvoices());
    try {
      actual = new InvoiceBook(mock, emailServiceMock)
          .getInvoiceById(mock.getInvoices().get(50).getId());
    } catch (IndexOutOfBoundsException e) {
      actual = null;
    }
    //then
    assertNull(actual);

  }

  @Test
  public void shouldFromNonTimePeriodTest() {

    //given

    LocalDate startDate = LocalDate.of(2016, 8, 10);
    LocalDate endDate = LocalDate.of(2016, 10, 10);

    //when
    when(mock.getInvoices()).thenReturn(newInvoices());

    boolean actual = new InvoiceBook(mock, emailServiceMock)
        .isFromTimePeriod(mock.getInvoices().get(0).getLocalDate(), startDate, endDate);

    boolean expected = false;

    //then
    assertEquals(expected, actual);
  }

}

