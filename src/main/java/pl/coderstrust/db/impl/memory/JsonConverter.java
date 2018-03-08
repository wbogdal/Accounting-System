package pl.coderstrust.db.impl.memory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.stereotype.Component;
import pl.coderstrust.model.Invoice;

@Component
public class JsonConverter {

  private final ObjectMapper mapper;

  public JsonConverter(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  public String convertFromInvoiceToJson(Invoice invoice) throws JsonProcessingException {

    String jsonInString = mapper.writeValueAsString(invoice);
    return jsonInString;
  }

  public Invoice convertFromJsonToInvoice(String thisLine) {
    Invoice obj = null;
    try {
      obj = mapper.readValue(thisLine, Invoice.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return obj;
  }

}
