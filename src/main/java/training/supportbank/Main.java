package training.supportbank;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.LogManager;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.io.File;
import java.io.FileNotFoundException;


public class Main {

    private static final Logger LOGGER = LogManager.getLogger("");

    public static void main(String args[]) {
        LOGGER.fine("This is a log message");
        List<User> users = loadCSV("DodgyTransactions2015.csv");
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Commads:\n1. List All\n2. List [Account]");
        System.out.print("Please enter an option: ");
        String command = sc.nextLine();
        if (command.equals("List All")){
            ListAll(users);
        }
        else if (command.contains("List")){
            String name = command.substring(5);
            User user = getUserByName(users, name);
            if (user == null) {
                System.out.println("User " + name + " doesn't exist.");
            }
            else {
                user.listUserTransactions();
            }
        }
        else{
            System.out.println("Invalid command.");
        }
        
    }

    public static List<User> loadCSV(String filename) {
        List<User> users = new ArrayList<>();
        try {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            sc.nextLine();
            //sc.next();
            while (sc.hasNextLine()) {
                String tmpstr = sc.nextLine();
                String[] trStr = tmpstr.split(",");
                String date = trStr[0];
                String from = trStr[1];
                String to = trStr[2];
                String narrative = trStr[3];
                double amount = Double.parseDouble(trStr[4]);
                Transaction tr = new Transaction(date, from, to, narrative, amount);
                User usFrom = getUserByName(users, from);
                User usTo = getUserByName(users, to);

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

    public static User getUserByName(List<User> users, String name){
        User user = null; 
        List<User> foundUsers = users.stream().filter((userL) -> userL.getName()
            .equals(name))
            .collect(Collectors.toList());

        if (foundUsers.isEmpty()){
            user = new User(name);
            users.add(user);
        }
        else
            user = foundUsers.get(0);
        return user;
    }

    public static void ListAll(List<User> users){
        users.stream().forEach((user) -> System.out.println(user.getName() + ": "
                                             + Math.round(user.getNetMoney()*100.0)/100.0));
    }
}
