package training.supportbank;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;

import java.time.*;

public class XMLLoader {

    public static List<User> loadXML(String filename) {
        List<User> users = new ArrayList<>();
        try {
            File file = new File(filename);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("SupportTransaction");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) node;
                    String date = extractDate(node);
                    String narrative = getItemFromNode(node, "Description");
                    double amount = Main.parseDoubleSafe(getItemFromNode(node, "Value"));

                    NodeList partiesNodeList = e.getElementsByTagName("Parties");
                    Node partiesNode = partiesNodeList.item(0);

                    String from = getItemFromNode(partiesNode, "From");
                    String to = getItemFromNode(partiesNode, "To");

                    Transaction tr = new Transaction(date, from, to, narrative, amount);
                    User usFrom = Main.getUserByName(users, from);
                    User usTo = Main.getUserByName(users, to);

                    usFrom.addTransactionFrom(tr);
                    usTo.addTransactionTo(tr);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return users;
    }

    public static String extractDate(Node node) {
        String shortDate = node.getAttributes().getNamedItem("Date").getNodeValue();
        int days = -1;
        try {
            days = Integer.parseInt(shortDate);
            LocalDate start = LocalDate.of(1900, 1, 1);
            return start.plusDays(days).minusDays(2).toString();
        } catch (Exception e) {
            System.out.println("Couldn't parse date " + shortDate);
            return "";
        }
    }

    public static String getItemFromNode(Node node, String fieldName) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element e = (Element) node;
            return e.getElementsByTagName(fieldName).item(0).getTextContent();
        }
        return "";
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
