package pl.coderstrust.rest;


import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class InvoicesControllerTest {

  @Autowired
  private MockMvc mockMvc;


  @Test
  public void Test1shouldCreateInvoice() throws Exception {

    String invoiceJson = "{\"id\":1,\"localDate\":\"2018-01-10\",\"seller\":{\"name\":\"Blue\","
        + "\"vatIdentificationNumber\":\"6565656565555\"},\"buyer\":{\"name\":\"Green\","
        + "\"vatIdentificationNumber\":\"7777777\"},\"items\":[{\"description\":\"Xiaomi\","
        + "\"value\":588888,\"vat\":\"VAT_23\"}]}";

    this.mockMvc.perform(post("/invoices/")
        .contentType("application/json;charset=UTF-8")
        .content(invoiceJson))
        .andExpect(status().isCreated());
  }

  @Test
  public void Test2shouldUpdateInvoiceById() throws Exception {
    String invoiceJson = "{\"id\":1,\"localDate\":\"2018-01-10\",\"seller\":{\"name\":\"Samsung\","
        + "\"vatIdentificationNumber\":\"56852012\"},\"buyer\":{\"name\":\"Dell\","
        + "\"vatIdentificationNumber\":\"99999999\"},\"items\":[{\"description\":\"hard driver\","
        + "\"value\":1998,\"vat\":\"VAT_23\"}]}";

    String invoiceJson2 = "{\"id\":1,\"localDate\":\"2018-01-09\",\"seller\":{\"name\":\"Blue\","
        + "\"vatIdentificationNumber\":\"6565656565555\"},\"buyer\":{\"name\":\"Green\","
        + "\"vatIdentificationNumber\":\"7777777\"},\"items\":[{\"description\":\"Xiaomi\","
        + "\"value\":588888,\"vat\":\"VAT_23\"}]}";

    this.mockMvc.perform(post("/invoices/")
        .contentType("application/json;charset=UTF-8")
        .content(invoiceJson))
        .andDo(print()).andExpect(status().isCreated());

    this.mockMvc.perform(put("/invoices/1")
        .contentType("application/json;charset=UTF-8")
        .content(invoiceJson2))
        .andDo(print()).andExpect(status().isOk());

    this.mockMvc.perform(get("/invoices/1"))
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        //   jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.seller.name", is("Blue")))
        .andExpect(jsonPath("$.buyer.name", is("Green")))
        .andExpect(jsonPath("$.items[0].description", is("Xiaomi")));
  }

  @Test
  public void Test3shouldReturnInvoiceById() throws Exception {
    this.mockMvc
        .perform(get("/invoices/1")
            .contentType("application/json;charset=UTF-8"))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)));
  }

  @Test
  public void Test4shouldDeleteInvoiceById() throws Exception {
    this.mockMvc
        .perform(delete("/invoices/1"))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(status().isOk());
  }


}

