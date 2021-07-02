package training.supportbank;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class CSVLoader {

    public static List<User> loadCSV(String filename) {
        List<User> users = new ArrayList<>();
        try {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            sc.nextLine();
            //sc.next();
            while (sc.hasNextLine()) {
                String tmpstr = sc.nextLine();
                Main.getLogger().info("Adding entry " + tmpstr);
                String[] trStr = tmpstr.split(",");
                String date = trStr[0];
                String from = trStr[1];
                String to = trStr[2];
                String narrative = trStr[3];
                double amount = Main.parseDoubleSafe(trStr[4]);
                
                Transaction tr = new Transaction(date, from, to, narrative, amount);
                User usFrom = Main.getUserByName(users, from);
                User usTo = Main.getUserByName(users, to);

                usFrom.addTransactionFrom(tr);
                usTo.addTransactionTo(tr);
            }
            sc.close();
        } 
        catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
        return users;
    }
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
    
}
