package training.supportbank;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import java.time.*;


public class ExportService {
    public static void writeCSV(String filename, List<Transaction> tr){
        try{
            File file = new File(filename);
            file.createNewFile();
            FileWriter writer = new FileWriter(filename);
            writer.write("Date,From,To,Narrative,Amount\n");
            for (Transaction trans : tr){
                writer.write(String.join(",", trans.getDate(), trans.getFrom(), 
                    trans.getTo(), trans.getNarrative(), Double.toString(trans.getAmount())));
                    writer.write("\n");
            }

            writer.close();
            System.out.println("Successfully exported to " + filename);
        }
        catch(IOException e){
            System.out.println("An error occured");
            e.printStackTrace();
        }
    }

    public static void writeJSON(String filename, List<Transaction> tr){
        try{
            File file = new File(filename);
            file.createNewFile();
            FileWriter writer = new FileWriter(filename);
            JsonArray list = new JsonArray();
            for (Transaction trans : tr){
                String jsonString = new Gson().toJson(trans);
                JsonParser parser = new JsonParser();
                JsonObject jsonObject = (JsonObject) parser.parse(jsonString);
                list.add(jsonObject);
            }

            writer.write(list.toString());
            writer.close();
            System.out.println("Successfully exported to " + filename);
        }
        catch(IOException e){
            System.out.println("An error occured");
            e.printStackTrace();
        }
    }

    
    public static void appendChild(Document doc, Element parent, String elementName, String contents) {
        Element e = doc.createElement(elementName);
        e.appendChild(doc.createTextNode(contents));
        parent.appendChild(e);
    }

    public static void writeXML(String filename, List<Transaction> tr) {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("TransactionList");
            document.appendChild(root);

            for (Transaction trans : tr) {
                Element SupportTransaction = document.createElement("SupportTransaction");
                root.appendChild(SupportTransaction);

                Attr attr = document.createAttribute("Date");
                long date = actualDateToXMLDate(trans.getDate());
                attr.setValue(Long.toString(date));
                SupportTransaction.setAttributeNode(attr);

                appendChild(document, SupportTransaction, "Description", trans.getNarrative());
                appendChild(document, SupportTransaction, "Value", Double.toString(trans.getAmount()));
                Element parties = document.createElement("Parties");
                SupportTransaction.appendChild(parties);
                appendChild(document, parties, "From", trans.getFrom());
                appendChild(document, parties, "To", trans.getTo());
            }
            // create the xml file
            // transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(filename));

            transformer.transform(domSource, streamResult);
            System.out.println("Successfully exported to " + filename);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

    public static long actualDateToXMLDate(String date) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(date, dtf);
        LocalDate start = LocalDate.of(1900, 1, 1);
        return Duration.between(start.atStartOfDay(), parsedDate.atStartOfDay()).toDays() + 2;
    }

    
}
