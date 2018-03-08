package pl.coderstrust.logic;


import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderstrust.logic.calculator.IncomeSumCalculator;
import pl.coderstrust.logic.calculator.PaidSumCalculator;
import pl.coderstrust.logic.calculator.VatToPayCalculator;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceItem;

@Component
public class CreatePDFProvider {

  private final VatToPayCalculator vatToPayCalculator;
  private final PaidSumCalculator paidSumCalculator;
  private final IncomeSumCalculator incomeSumCalculator;
  private final InvoiceBook invoiceBook;

  @Autowired
  public CreatePDFProvider(VatToPayCalculator vatToPayCalculator,
      PaidSumCalculator paidSumCalculator, IncomeSumCalculator incomeSumCalculator,
      InvoiceBook invoiceBook) {
    this.vatToPayCalculator = vatToPayCalculator;
    this.paidSumCalculator = paidSumCalculator;
    this.incomeSumCalculator = incomeSumCalculator;
    this.invoiceBook = invoiceBook;
  }

  public void createPdf(Invoice invoice, String dest) throws IOException {

    PdfWriter writer = new PdfWriter(dest);

    PdfDocument pdf = new PdfDocument(writer);

    Document document = new Document(pdf);

    PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);

    document.add(new Paragraph("FAKTURA NR:").setFont(font).setFixedPosition(1, 200, 800, 100));
    document.add(
        new Paragraph((String.valueOf(invoice.getId())) + " / " + invoice.getLocalDate().getYear())
            .setFont(font).setFixedPosition(1, 280, 800, 100));

    document.add(new Paragraph("SPRZEDAWCA:").setFont(font).setFixedPosition(1, 10, 760, 100));

    document.add(new Paragraph("NAZWA / FIRMA:").setFont(font).setFixedPosition(1, 10, 735, 100));
    document.add(new Paragraph(invoice.getSeller().getName()).setFont(font)
        .setFixedPosition(1, 110, 735, 100));

    document.add(new Paragraph("NIP:").setFont(font).setFixedPosition(1, 10, 720, 100));
    document.add(new Paragraph(invoice.getSeller().getVatIdentificationNumber()).setFont(font)
        .setFixedPosition(1, 110, 720, 100));
    document.add(new Paragraph("ADRES:").setFont(font).setFixedPosition(1, 10, 705, 100));

    document.add(new Paragraph("NABYWCA:").setFont(font).setFixedPosition(1, 350, 760, 100));

    document.add(new Paragraph("NAZWA / FIRMA:").setFont(font).setFixedPosition(1, 350, 735, 100));
    document.add(new Paragraph(invoice.getBuyer().getName()).setFont(font)
        .setFixedPosition(1, 450, 735, 100))
        .add(new Paragraph("NIP:").setFont(font).setFixedPosition(1, 350, 720, 100))
        .add(new Paragraph(invoice.getBuyer().getVatIdentificationNumber()).setFont(font)
            .setFixedPosition(1, 450, 720, 100))
        .add(new Paragraph("ADRES:").setFont(font).setFixedPosition(1, 350, 705, 100));

    document.add(itemsTable(invoice.getItems()));
    document.add(priceSummary(invoice.getItems(), invoice));

    document.close();

  }

  public Table itemsTable(List<InvoiceItem> invoiceItems) {

    Table table = new Table(new float[]{4, 1, 3, 4});
    table.setWidthPercent(100).setFixedPosition(10, 500, 550);

    Cell ordinalNumber = new Cell().add("lp");
    table.addCell(ordinalNumber);
    Cell productDescription = new Cell().add("Nazwa towaru / uslugi");
    table.addCell(productDescription);
    Cell VAT = new Cell().add("VAT");
    table.addCell(VAT);

    Cell grossPrice = new Cell().add("Cena brutto");
    table.addCell(grossPrice);
    int counter = 1;
    for (InvoiceItem invoiceItem : invoiceItems) {
      table.addCell(String.valueOf(counter));
      table.addCell(invoiceItem.getDescription());
      table.addCell(String.valueOf(invoiceItem.getVat()));
      table.addCell(String.valueOf(invoiceItem.getValue()));
      counter++;
    }
    return table;
  }

  public Table priceSummary(List<InvoiceItem> invoiceItems, Invoice invoice) {

    Table table1 = new Table(new float[]{4, 1, 3, 4});
    table1.setWidthPercent(100).setFixedPosition(250, 400, 300);
    Cell vatRate = new Cell().add("Stawka VAT");
    table1.addCell(vatRate);
    Cell netPrice = new Cell().add("Netto");
    table1.addCell(netPrice);
    Cell vatAmount = new Cell().add("Kwota VAT");
    table1.addCell(vatAmount);
    Cell grossPrice1 = new Cell().add("Brutto");
    table1.addCell(grossPrice1);

    double netValue =
        incomeSumCalculator.visit(invoice).doubleValue() - vatToPayCalculator.visit(invoice)
            .doubleValue();

    table1.addCell(String.valueOf(invoiceItems.get(0).getVat()));
    table1.addCell(String.valueOf(netValue));
    table1.addCell(String.valueOf(vatToPayCalculator.visit(invoice)));
    table1.addCell(String.valueOf(incomeSumCalculator.visit(invoice)));

    return table1;
  }
}
