package pl.coderstrust.logic;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.FilteredEventListener;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.text.DocumentException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import pl.coderstrust.db.impl.memory.InMemoryDatabase;
import pl.coderstrust.logic.calculator.IncomeSumCalculator;
import pl.coderstrust.logic.calculator.PaidSumCalculator;
import pl.coderstrust.logic.calculator.VatToPayCalculator;
import pl.coderstrust.mail.EmailServiceImpl;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceItem;
import pl.coderstrust.model.Vat;

public class CreatePDFProviderTest {

  String path = "src/test/resources/testInvoice.pdf";
  File file = new File(path);
  final EmailServiceImpl emailServiceMock = mock(EmailServiceImpl.class);

  @After
  public void cleanUp() {
    file.delete();
  }

  @Test
  public void createPDFProvider() throws IOException, DocumentException {
    VatToPayCalculator vatToPayCalculator = new VatToPayCalculator();
    PaidSumCalculator paidSumCalculator = new PaidSumCalculator();
    IncomeSumCalculator incomeSumCalculator = new IncomeSumCalculator();
    InMemoryDatabase database = new InMemoryDatabase();

    InvoiceBook invoiceBook = new InvoiceBook(database, emailServiceMock);
    InvoiceFactory invoiceFactory = new InvoiceFactory(database);
    Invoice invoice = new Invoice();
    CreatePDFProvider createPDFProvider = new CreatePDFProvider(vatToPayCalculator,
        paidSumCalculator, incomeSumCalculator, invoiceBook);

    List<InvoiceItem> invoiceItems = new ArrayList<>();

    Company seller1 = new Company();
    seller1.setName("CodersTrust");
    seller1.setVatIdentificationNumber("6565656565555");
    Company buyer1 = new Company();
    buyer1.setName("Green");
    buyer1.setVatIdentificationNumber("7777777");

    invoiceItems.add(new InvoiceItem("Xiaomi", BigDecimal.valueOf(100), Vat.VAT_23));

    invoice = invoiceFactory.create(LocalDate.now(), seller1, buyer1, invoiceItems);

    //when
    createPDFProvider.createPdf(invoice, path);

    //then
    assertTrue(file.exists());
  }


  @Test
  public void manipulatePdf() throws IOException {

    //given

    VatToPayCalculator vatToPayCalculator = new VatToPayCalculator();
    PaidSumCalculator paidSumCalculator = new PaidSumCalculator();
    IncomeSumCalculator incomeSumCalculator = new IncomeSumCalculator();
    InMemoryDatabase database = new InMemoryDatabase();
    InvoiceBook invoiceBook = new InvoiceBook(database, emailServiceMock);
    InvoiceFactory invoiceFactory = new InvoiceFactory(database);
    Invoice invoice = new Invoice();
    CreatePDFProvider createPDFProvider = new CreatePDFProvider(vatToPayCalculator,
        paidSumCalculator, incomeSumCalculator, invoiceBook);

    List<InvoiceItem> invoiceItems = new ArrayList<>();
    Company seller1 = new Company();
    seller1.setName("CodersTrust");
    seller1.setVatIdentificationNumber("6565656565555");
    Company buyer1 = new Company();
    buyer1.setName("Green");
    buyer1.setVatIdentificationNumber("7777777");

    invoiceItems.add(new InvoiceItem("Xiaomi", BigDecimal.valueOf(100), Vat.VAT_23));

    invoice = invoiceFactory.create(LocalDate.now(), seller1, buyer1, invoiceItems);
    createPDFProvider.createPdf(invoice, path);

    String expectedText = "FAKTURA NR:" +
        invoice.getId() + " / 2018\n" + "SPRZEDAWCA: NABYWCA:\n" + "NAZWA / FIRMA: " + invoice
        .getSeller()
        .getName() + " NAZWA / FIRMA: " + invoice.getBuyer().getName() + "\n" + "NIP: " + invoice
        .getSeller().getVatIdentificationNumber() + " NIP: " + invoice.getBuyer()
        .getVatIdentificationNumber() + "\n"
        + "ADRES: ADRES:\n" + "lp Nazwa towaru / uslugi VAT Cena brutto\n"
        + "1 Xiaomi VAT_23 100\n" + "Stawka VAT Netto Kwota VAT Brutto\n"
        + "VAT_23 77.0 23.00 100.00";

    //when
    File file = new File(path);
    file.getParentFile().mkdirs();
    PdfDocument pdfDoc = new PdfDocument(new PdfReader(path));
    Rectangle rect = new Rectangle(36, 750, 523, 56);

    FilteredEventListener listener = new FilteredEventListener();
    LocationTextExtractionStrategy extractionStrategy = listener
        .attachEventListener(new LocationTextExtractionStrategy());
    new PdfCanvasProcessor(listener).processPageContent(pdfDoc.getFirstPage());
    String actualText = extractionStrategy.getResultantText();
    System.out.println(actualText);
    pdfDoc.close();

    //then
    Assert.assertEquals(expectedText, actualText);
  }
}
