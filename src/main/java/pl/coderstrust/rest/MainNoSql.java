package pl.coderstrust.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import pl.coderstrust.db.impl.memory.JsonConverter;
import pl.coderstrust.model.Invoice;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MainNoSql {


  public static void main(String[] args) throws UnknownHostException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    JsonConverter jsonConverter=new JsonConverter(objectMapper);

    MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));

    DB database = mongoClient.getDB("xyz");
    DBCollection collection = database.getCollection("Invoice");

//    MongoTemplate mongoTemplate=new MongoTemplate(mongoClient,"xyz");


        DBCursor cursor = collection.find();
    List<Invoice> myinvoices=new ArrayList<>();
    while(cursor.hasNext()) {
      myinvoices.add(jsonConverter.convertFromJsonToInvoice(cursor.next().toString()));

    }



//    List<Invoice> myinvoices=new ArrayList<>();
//    DBCursor curr = collection.find();
//
//    while (curr.hasNext()) {
//      DBObject obj =curr.next();
//      Invoice invoice= mongoTemplate.getConverter().read(Invoice.class, obj);
//      myinvoices.add(invoice);
//    }


//    DBCollection idCollection = database.getCollection("Id");
//
//    Company seller1 = new Company();
//    seller1.setName("Blue");
//    seller1.setVatIdentificationNumber("6565656565555");
//    Company buyer1 = new Company();
//    buyer1.setName("Green");
//    buyer1.setVatIdentificationNumber("7777777");
//    List<InvoiceItem> invoiceItems = new ArrayList<>();
//    invoiceItems.add(new InvoiceItem("Xiaomi", BigDecimal.valueOf(588888), Vat.VAT_23));
//    invoiceItems.add(new InvoiceItem("Budomi", BigDecimal.valueOf(65), Vat.VAT_23));
//
//    Company seller2 = new Company();
//    seller2.setName("Blue");
//    seller2.setVatIdentificationNumber("6565656565555");
//    Company buyer2 = new Company();
//    buyer2.setName("Green");
//    buyer2.setVatIdentificationNumber("7777777");
//    List<InvoiceItem> invoiceItems2 = new ArrayList<>();
//    invoiceItems2.add(new InvoiceItem("Xiaomi", BigDecimal.valueOf(588888), Vat.VAT_23));
//    invoiceItems2.add(new InvoiceItem("Budomi", BigDecimal.valueOf(65), Vat.VAT_23));
//
//    Invoice invoice = new Invoice(5, LocalDate.now(), seller1, buyer1, invoiceItems);
//
//    Invoice invoice2 = new Invoice(1, LocalDate.now(), seller2, buyer2, invoiceItems2);
//
//    List<BasicDBObject> docItems = new ArrayList<>();
//    for (int i = 0; i < invoiceItems.size(); i++) {
//
//      docItems.add(new BasicDBObject("description", invoice.getItems().get(i).getDescription())
//          .append("value", String.valueOf(invoice.getItems().get(i).getValue()))
//          .append("vat", String.valueOf(invoice.getItems().get(i).getVat())));
//    }
//
//    BasicDBObject doc = new BasicDBObject("_id", invoice.getId())
//        .append("buyer", new BasicDBObject("name", invoice.getBuyer().getName())
//            .append("vatIdentificationNumber", invoice.getBuyer().getVatIdentificationNumber()))
//        .append("items", docItems).append("localDate", String.valueOf(invoice.getLocalDate()))
//        .append("seller", new BasicDBObject("name", invoice.getSeller().getName())
//            .append("vatIdentificationNumber", invoice.getSeller().getVatIdentificationNumber()));
//
//    List<BasicDBObject> docItems2 = new ArrayList<>();
//    for (int i = 0; i < invoiceItems2.size(); i++) {
//
//      docItems2.add(new BasicDBObject("description", invoice2.getItems().get(i).getDescription())
//          .append("value", String.valueOf(invoice2.getItems().get(i).getValue()))
//          .append("vat", String.valueOf(invoice2.getItems().get(i).getVat())));
//    }
//
//    BasicDBObject doc2 = new BasicDBObject("_id", invoice2.getId())
//        .append("buyer", new BasicDBObject("name", invoice2.getBuyer().getName())
//            .append("vatIdentificationNumber", invoice2.getBuyer().getVatIdentificationNumber()))
//        .append("items", docItems2).append("localDate", String.valueOf(invoice2.getLocalDate()))
//        .append("seller", new BasicDBObject("name", invoice2.getSeller().getName())
//            .append("vatIdentificationNumber", invoice2.getSeller().getVatIdentificationNumber()));

//    BasicDBObject id = new BasicDBObject("_id",invoice.getId());
//    idCollection.insert(id);
//    collection.insert(doc, doc2);

//    DBCursor curr = collection.find().sort(new BasicDBObject("_id", -1)).limit(1);
//    if (curr.hasNext()) {
//     int price = (int) curr.next().get("_id");
//      System.out.println(price);
//
//    }

    }

    //remove invoice
//    int id=0;
//collection.remove(new BasicDBObject().append("_id", id));

//getInvoices

//    DBCursor cursor = collection.find();
//    while(cursor.hasNext()) {
//      System.out.println(cursor.next());
//    }
//
//    DBCursor cursor1 = collection.find();
//    while(cursor1.hasNext()) {
//
//    }
//BasicDBList invoicesList= new BasicDBList();
//invoicesList.add(cursor1);
//
//    ObjectId id = doc.getObjectId("_id");
//  }

  }



