package pl.coderstrust.db.impl.memory;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.stereotype.Component;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceItem;

import java.util.ArrayList;
import java.util.List;

@Component
public class DBObjectConverter {


  public List<BasicDBObject> invoiceItemsConverter(Invoice invoice) {

    List<BasicDBObject> docItems = new ArrayList<>();
    List<InvoiceItem> invoiceItems = invoice.getItems();
    for (int i = 0; i < invoiceItems.size(); i++) {
      docItems.add(new BasicDBObject("description", invoice.getItems().get(i).getDescription())
          .append("value", String.valueOf(invoice.getItems().get(i).getValue()))
          .append("vat", String.valueOf(invoice.getItems().get(i).getVat())));
    }
    return docItems;
  }

  public DBObject convertInvoiceObjectIntoMongoDocument(Invoice invoice) {
    BasicDBObject doc = new BasicDBObject("_id", invoice.getId()).append("id", invoice.getId())
        .append("buyer", new BasicDBObject("name", invoice.getBuyer().getName())
            .append("vatIdentificationNumber", invoice.getBuyer().getVatIdentificationNumber()))
        .append("items", invoiceItemsConverter(invoice))
        .append("localDate", String.valueOf(invoice.getLocalDate()))
        .append("seller", new BasicDBObject("name", invoice.getSeller().getName())
            .append("vatIdentificationNumber", invoice.getSeller().getVatIdentificationNumber()));
    return doc;
  }


}
