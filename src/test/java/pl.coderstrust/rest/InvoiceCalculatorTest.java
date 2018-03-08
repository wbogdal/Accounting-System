package pl.coderstrust.rest;


import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class InvoiceCalculatorTest {

  @Autowired
  private MockMvc mockMvc;


  @Test
  public void shouldReturnPaidSum() throws Exception {
    String invoiceJson = "{\"id\":3,\"localDate\":\"2018-01-09\",\"seller\":{\"name\":\"Blue\","
        + "\"vatIdentificationNumber\":\"6565656565555\"},\"buyer\":{\"name\":\"Green\","
        + "\"vatIdentificationNumber\":\"7777777\"},\"items\":[{\"description\":\"Xiaomi\","
        + "\"value\":100,\"vat\":\"VAT_23\"}]}";

    String invoiceJson1 =
        "{\"id\":4,\"localDate\":\"2018-01-09\",\"seller\":{\"name\":\"CodersTrust\","
            + "\"vatIdentificationNumber\":\"6565656565555\"},\"buyer\":{\"name\":\"Green\","
            + "\"vatIdentificationNumber\":\"7777777\"},\"items\":[{\"description\":\"Xiaomi\","
            + "\"value\":50000,\"vat\":\"VAT_23\"}]}";

    this.mockMvc.perform(post("/invoices/")
        .contentType("application/json;charset=UTF-8")
        .content(invoiceJson))
        .andExpect(status().isCreated());

    this.mockMvc.perform(post("/invoices/")
        .contentType("application/json;charset=UTF-8")
        .content(invoiceJson1))
        .andExpect(status().isCreated());

    this.mockMvc
        .perform(get("/invoices/paidsum/1/2018/")).andDo(print())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(jsonPath("$", is(100.0)));

  }

  @Test
  public void shouldReturnIncomeSum() throws Exception {

    String invoiceJson = "{\"id\":1,\"localDate\":\"2018-01-09\",\"seller\":{\"name\":\"Blue\","
        + "\"vatIdentificationNumber\":\"6565656565555\"},\"buyer\":{\"name\":\"Green\","
        + "\"vatIdentificationNumber\":\"7777777\"},\"items\":[{\"description\":\"Xiaomi\","
        + "\"value\":100,\"vat\":\"VAT_23\"}]}";

    String invoiceJson1 =
        "{\"id\":2,\"localDate\":\"2018-01-09\",\"seller\":{\"name\":\"CodersTrust\","
            + "\"vatIdentificationNumber\":\"6565656565555\"},\"buyer\":{\"name\":\"Green\","
            + "\"vatIdentificationNumber\":\"7777777\"},\"items\":[{\"description\":\"Xiaomi\","
            + "\"value\":50000,\"vat\":\"VAT_23\"}]}";

    this.mockMvc.perform(post("/invoices/")
        .contentType("application/json;charset=UTF-8")
        .content(invoiceJson))
        .andExpect(status().isCreated());

    this.mockMvc.perform(post("/invoices/")
        .contentType("application/json;charset=UTF-8")
        .content(invoiceJson1))
        .andExpect(status().isCreated());

    this.mockMvc
        .perform(get("/invoices/sum/1/2018/")).andDo(print())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(jsonPath("$", is(50000.0)));

  }

  @Test
  public void shouldCountVatToPay() throws Exception {

    String invoiceJson =
        "{\"id\":1,\"localDate\":\"2018-01-10\",\"seller\":{\"name\":\"CodersTrust\","
            + "\"vatIdentificationNumber\":\"6565656565555\"},\"buyer\":{\"name\":\"Green\","
            + "\"vatIdentificationNumber\":\"7777777\"},\"items\":[{\"description\":\"Xiaomi\","
            + "\"value\":500,\"vat\":\"VAT_23\"}]}";

    String invoiceJson1 =
        "{\"id\":2,\"localDate\":\"2018-01-10\",\"seller\":{\"name\":\"CodersTrust\","
            + "\"vatIdentificationNumber\":\"6565656565555\"},\"buyer\":{\"name\":\"Green\","
            + "\"vatIdentificationNumber\":\"7777777\"},\"items\":[{\"description\":\"Xiaomi\","
            + "\"value\":200,\"vat\":\"VAT_23\"}]}";

    this.mockMvc.perform(post("/invoices/")
        .contentType("application/json;charset=UTF-8")
        .content(invoiceJson))
        .andExpect(status().isCreated());

    this.mockMvc.perform(post("/invoices/")
        .contentType("application/json;charset=UTF-8")
        .content(invoiceJson1))
        .andExpect(status().isCreated());

    this.mockMvc
        .perform(get("/invoices/vattopay?month=1&year=2018")).andDo(print())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(jsonPath("$", is(161.00)));

  }

  @Test
  public void shouldCountOverpaidVat() throws Exception {

    String invoiceJson = "{\"id\":1,\"localDate\":\"2018-01-10\",\"seller\":{\"name\":\"Blue\","
        + "\"vatIdentificationNumber\":\"6565656565555\"},\"buyer\":{\"name\":\"Green\","
        + "\"vatIdentificationNumber\":\"7777777\"},\"items\":[{\"description\":\"Xiaomi\","
        + "\"value\":500,\"vat\":\"VAT_23\"}]}";

    String invoiceJson1 =
        "{\"id\":2,\"localDate\":\"2018-01-10\",\"seller\":{\"name\":\"Orange\","
            + "\"vatIdentificationNumber\":\"6565656565555\"},\"buyer\":{\"name\":\"CodersTrust\","
            + "\"vatIdentificationNumber\":\"7777777\"},\"items\":[{\"description\":\"Xiaomi\","
            + "\"value\":200,\"vat\":\"VAT_23\"}]}";

    this.mockMvc.perform(post("/invoices/")
        .contentType("application/json;charset=UTF-8")
        .content(invoiceJson))
        .andExpect(status().isCreated());

    this.mockMvc.perform(post("/invoices/")
        .contentType("application/json;charset=UTF-8")
        .content(invoiceJson1))
        .andExpect(status().isCreated());

    this.mockMvc
        .perform(get("/invoices/overpaidvat?month=1&year=2018")).andDo(print())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(jsonPath("$", is(161.00)));

  }

  @Test
  public void shouldCountVatDifference() throws Exception {

    String invoiceJson =
        "{\"id\":1,\"localDate\":\"2018-01-10\",\"seller\":{\"name\":\"CodersTrust\","
            + "\"vatIdentificationNumber\":\"6565656565555\"},\"buyer\":{\"name\":\"Green\","
            + "\"vatIdentificationNumber\":\"7777777\"},\"items\":[{\"description\":\"Xiaomi\","
            + "\"value\":500,\"vat\":\"VAT_23\"}]}";

    String invoiceJson1 =
        "{\"id\":2,\"localDate\":\"2018-01-10\",\"seller\":{\"name\":\"Orange\","
            + "\"vatIdentificationNumber\":\"6565656565555\"},\"buyer\":{\"name\":\"CodersTrust\","
            + "\"vatIdentificationNumber\":\"7777777\"},\"items\":[{\"description\":\"Xiaomi\","
            + "\"value\":200,\"vat\":\"VAT_23\"}]}";

    this.mockMvc.perform(post("/invoices/")
        .contentType("application/json;charset=UTF-8")
        .content(invoiceJson))
        .andExpect(status().isCreated());

    this.mockMvc.perform(post("/invoices/")
        .contentType("application/json;charset=UTF-8")
        .content(invoiceJson1))
        .andExpect(status().isCreated());

    this.mockMvc
        .perform(get("/invoices/vatdifference?month=1&year=2018")).andDo(print())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(jsonPath("$", is(69.00)));

  }

  @Test
  public void shouldCountMonthlyIncomeTax() throws Exception {

    String invoiceJson =
        "{\"id\":1,\"localDate\":\"2018-01-10\",\"seller\":{\"name\":\"CodersTrust\","
            + "\"vatIdentificationNumber\":\"6565656565555\"},\"buyer\":{\"name\":\"Green\","
            + "\"vatIdentificationNumber\":\"7777777\"},\"items\":[{\"description\":\"Xiaomi\","
            + "\"value\":10000,\"vat\":\"VAT_23\"}]}";

    String invoiceJson1 =
        "{\"id\":2,\"localDate\":\"2018-01-31\",\"seller\":{\"name\":\"Orange\","
            + "\"vatIdentificationNumber\":\"6565656565555\"},\"buyer\":{\"name\":\"CodersTrust\","
            + "\"vatIdentificationNumber\":\"7777777\"},\"items\":[{\"description\":\"Xiaomi\","
            + "\"value\":100,\"vat\":\"VAT_23\"}]}";

    this.mockMvc.perform(post("/invoices/")
        .contentType("application/json;charset=UTF-8")
        .content(invoiceJson))
        .andExpect(status().isCreated());

    this.mockMvc.perform(post("/invoices/")
        .contentType("application/json;charset=UTF-8")
        .content(invoiceJson1))
        .andExpect(status().isCreated());
    System.out.println(get("/invoices/1"));

    this.mockMvc
        .perform(
            get("/invoices/monthlyincometax?month=1&year=2018&healthInsuranceAmount=100.0"
                + "&socialInsuranceAmount=100.0&taxFreeAmount=100.0"))
        .andDo(print()).andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(jsonPath("$", is(1564)));

  }

  @Test
  public void shouldCountYearlyIncomeTax() throws Exception {

    String invoiceJson =
        "{\"id\":1,\"localDate\":\"2018-01-10\",\"seller\":{\"name\":\"CodersTrust\","
            + "\"vatIdentificationNumber\":\"6565656565555\"},\"buyer\":{\"name\":\"Green\","
            + "\"vatIdentificationNumber\":\"7777777\"},\"items\":[{\"description\":\"Xiaomi\","
            + "\"value\":10000,\"vat\":\"VAT_23\"}]}";

    String invoiceJson1 =
        "{\"id\":2,\"localDate\":\"2018-02-27\",\"seller\":{\"name\":\"Orange\","
            + "\"vatIdentificationNumber\":\"6565656565555\"},\"buyer\":{\"name\":\"CodersTrust\","
            + "\"vatIdentificationNumber\":\"7777777\"},\"items\":[{\"description\":\"Xiaomi\","
            + "\"value\":100,\"vat\":\"VAT_23\"}]}";

    this.mockMvc.perform(post("/invoices/")
        .contentType("application/json;charset=UTF-8")
        .content(invoiceJson))
        .andExpect(status().isCreated());

    this.mockMvc.perform(post("/invoices/")
        .contentType("application/json;charset=UTF-8")
        .content(invoiceJson1))
        .andExpect(status().isCreated());
    System.out.println(get("/invoices/1"));

    this.mockMvc
        .perform(
            get("/invoices/yearlyincometax?month=2&year=2018&healthInsuranceAmount=100.0"
                + "&socialInsuranceAmount=100.0&taxFreeAmount=100.0"))
        .andDo(print()).andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(jsonPath("$", is(1564)));

  }
}
