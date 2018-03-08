package pl.coderstrust.db.impl.memory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FileHelper {


  public void appendInvoice(List<String> fileContent, File invoiceFile) throws IOException {
    FileWriter writer = new FileWriter(invoiceFile, true);

    for (int i = 0; i < fileContent.size(); i++) {
      String str = fileContent.get(i);
      writer.append("\n");
      writer.append(str);
    }

    writer.close();
  }


  public void saveNewInvoice(List<String> fileContent, File invoiceFile) throws IOException {

    FileWriter writer = new FileWriter(invoiceFile);

    for (int i = 0; i < fileContent.size(); i++) {
      String str = fileContent.get(i);
      writer.write(str);
      if (i < fileContent.size() - 1) {
        writer.write("\n");
      }
    }
    writer.close();

  }

  public List<String> readFromFile(File invoiceFile) throws IOException {
    List<String> fileContent = new ArrayList<>();
    if (!invoiceFile.isDirectory() && invoiceFile.exists()) {
      String thisLine;
      FileReader fileReader = new FileReader(invoiceFile);

      BufferedReader br = new BufferedReader(fileReader);

      while ((thisLine = br.readLine()) != null) {
        fileContent.add(thisLine);

        System.out.println(thisLine);
      }
      fileReader.close();
    }
    return fileContent;

  }
}








