package training.supportbank;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;

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
    
}
