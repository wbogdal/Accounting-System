package pl.coderstrust.mail;


import pl.coderstrust.model.Invoice;

public interface EmailSender {

  void sendEmail(String to, Invoice invoice);

}