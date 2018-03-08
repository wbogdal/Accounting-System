package pl.coderstrust.db.impl.memory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.coderstrust.db.Database;
import pl.coderstrust.model.Invoice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

//@Primary
@Repository
public class MongoDatabase implements Database, IdGenerator {

  private final JsonConverter converter;
  private final DBObjectConverter dbObjectConverter;
  private final AtomicInteger counter = new AtomicInteger();
  private MongoClient mongoClient = new MongoClient();
  private DB database = mongoClient.getDB("xyz");
  private DBCollection collection = database.getCollection("Invoice");

  @Autowired
  public MongoDatabase(JsonConverter converter, DBObjectConverter dbObjectConverter) {
    this.converter = converter;
    this.dbObjectConverter = dbObjectConverter;
  }

  @Override
  public void saveInvoice(Invoice invoice) {
    collection.insert(dbObjectConverter.convertInvoiceObjectIntoMongoDocument(invoice));
  }

  @Override
  public void removeInvoice(int id) {
    collection.remove(new BasicDBObject().append("_id", id));
  }

  @Override
  public int getLastId() {
    int lastId = 0;
    DBCursor curr = collection.find().sort(new BasicDBObject("_id", -1)).limit(1);
    if (curr.hasNext()) {
      lastId = (int) curr.next().get("_id");
    }
    return lastId;
  }

  @Override
  public List<Invoice> getInvoices() {

    DBCursor cursor = collection.find();
    List<Invoice> myinvoices = new ArrayList<>();
    while (cursor.hasNext()) {
      myinvoices.add(converter.convertFromJsonToInvoice(cursor.next().toString()));
    }
    return myinvoices;
  }

  public int generate() {
    counter.set(getLastId());
    return counter.incrementAndGet();
  }

  public List<Invoice> getInvoicesFromCurrentDay() {
    List<Invoice> invoices = getInvoices();
    return invoices.stream().filter(invoice -> invoice.getLocalDate().isEqual(LocalDate.now()))
        .collect(Collectors.toList());
  }
}
