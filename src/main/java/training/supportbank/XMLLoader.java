package training.supportbank;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.DocumentBuilder;  
import org.w3c.dom.Document;  
import org.w3c.dom.NodeList;  
import org.w3c.dom.Node;  
import org.w3c.dom.Element;  



public class XMLLoader {

    public static List<User> loadXML(String filename) {
        List<User> users = new ArrayList<>();
        try{
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
        }
        catch(Exception e){
            System.out.println(e);
        }
        return users;
    }

    public static String extractDate(Node node){
        String shortDate = node.getAttributes().getNamedItem("Date").getNodeValue();
        int days = -1;
        try {
            days = Integer.parseInt(shortDate);
            LocalDate start = LocalDate.of(1900, 1, 1);
            return start.plusDays(days).minusDays(2).toString();
        }
        catch (Exception e){
            System.out.println("Couldn't parse date " + shortDate);
            return "";
        }
        
    }

    public static String getItemFromNode(Node node, String fieldName){
        if (node.getNodeType() == Node.ELEMENT_NODE) {  
            Element e = (Element) node;  
            return e.getElementsByTagName(fieldName).item(0).getTextContent();
        }
        return "";
    }
    
}
