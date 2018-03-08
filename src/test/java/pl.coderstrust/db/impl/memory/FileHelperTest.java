package pl.coderstrust.db.impl.memory;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import pl.coderstrust.config.ObjectMapperProvider;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceItem;
import pl.coderstrust.model.Vat;


public class FileHelperTest {


  String path = "src/test1";

  File file = new File(path + LocalDate.now());
  FileHelper fileHelper = new FileHelper();
  JsonConverter converter;

  @Before
  public void setUp() {
//    file.delete();
    ObjectMapperProvider objectMapperProvider = new ObjectMapperProvider();
    converter = new JsonConverter(objectMapperProvider.mapper());
  }

  @Test
  public void appendInvoice() throws Exception {
    //given
    final List<Invoice> myListToSafe = new ArrayList();
    final List<String> listInJson = new ArrayList<>();

    Company seller1 = new Company();
    seller1.setName("Blue");
    seller1.setVatIdentificationNumber("6565656565555");
    Company buyer1 = new Company();
    buyer1.setName("Green");
    buyer1.setVatIdentificationNumber("7777777");

    List<InvoiceItem> invoiceItemList1 = Arrays
        .asList(new InvoiceItem("Xiaomi", BigDecimal.valueOf(588888), Vat.VAT_23));

    Invoice invoice1 = new Invoice(1, LocalDate.now(), seller1, buyer1, invoiceItemList1);

    Arrays.asList(myListToSafe.add(invoice1));

    for (Invoice myInvoice : myListToSafe) {
      try {
        listInJson.add(converter.convertFromInvoiceToJson(myInvoice));
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    }
    fileHelper.saveNewInvoice(listInJson, file);

    final List<Invoice> myListToSafe2 = new ArrayList();
    final List<String> listInJson2 = new ArrayList<>();

    Company seller3 = new Company();
    seller3.setName("Black");
    seller3.setVatIdentificationNumber("0709605954");
    Company buyer3 = new Company();
    buyer3.setName("White");
    buyer3.setVatIdentificationNumber("15603487");

    List<InvoiceItem> invoiceItemList = Arrays
        .asList(new InvoiceItem("Samsung", BigDecimal.valueOf(566), Vat.VAT_23));

    Invoice invoice = new Invoice(2, LocalDate.now(), seller3, buyer3, invoiceItemList);

    myListToSafe2.add(invoice);

    for (Invoice myInvoice : myListToSafe2) {
      try {
        listInJson2.add(converter.convertFromInvoiceToJson(myInvoice));
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    }

    //when
    fileHelper.appendInvoice(listInJson2, file);

    //then
    int expected = fileHelper.readFromFile(file).size();

    assertEquals(expected, 2);
  }


}
