package pl.coderstrust;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.coderstrust.logic.InvoiceBook;
import pl.coderstrust.logic.InvoiceFactory;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.InvoiceItem;
import pl.coderstrust.model.Vat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WebLayerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean

  private InvoiceBook invoiceBook;

  @Test
  public void shouldReturnDefaultMessage() throws Exception {
    Company company1 = new Company();
    company1.setName("Orange");
    company1.setVatIdentificationNumber("659445");

    Company company2 = new Company();
    company2.setName("Plus");
    company2.setVatIdentificationNumber("111111");

    InvoiceItem invoiceItem = new InvoiceItem();
    invoiceItem.setDescription("Xbox");
    invoiceItem.setValue(BigDecimal.valueOf(200));
    invoiceItem.setVat(Vat.VAT_23);
    String date = String.valueOf(LocalDate.now());

    InvoiceFactory invoiceFactory = new InvoiceFactory(() -> 0);

    when(invoiceBook.getInvoices()).thenReturn(Arrays.asList(
        invoiceFactory.create(LocalDate.now(), company1, company2, Arrays.asList(invoiceItem))));
    this.mockMvc.perform(get("/invoices"))
        .andExpect(content().contentType("application/json;charset=UTF-8")).andExpect(
        jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(0)))
        .andExpect(jsonPath("$[0].localDate", is(date)))
        .andExpect(jsonPath("$[0].seller.name", is("Orange")))
        .andExpect(jsonPath("$[0].seller.vatIdentificationNumber", is("659445")))
        .andExpect(jsonPath("$[0].buyer.name", is("Plus")))
        .andExpect(jsonPath("$[0].buyer.vatIdentificationNumber", is("111111")))
        .andExpect(jsonPath("$[0].items[0].description", is("Xbox")))
        .andExpect(jsonPath("$[0].items[0].value", is(200)))
        .andExpect(jsonPath("$[0].items[0].vat", is("VAT_23")))
    ;
  }
}
