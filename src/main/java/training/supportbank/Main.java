package training.supportbank;

import java.util.List;
import java.util.Scanner;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.stream.Collectors;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String args[]) throws Exception {
        if (args.length > 0){
            List<User> users = getFileNameFromUser(args[0]);
            if (users != null){
                String command = getCommandFromUser();
                if (command.equals("List All"))
                    ListAll(users);
                else if (command.contains("List"))
                    listUserTransactions(users, command);
                else
                    System.out.println("Invalid command.");
            }
        }
        else
            System.out.println("Filename not given in arguments.");
    }   

    public static List<User> getFileNameFromUser(String fileName) throws Exception{
       
        if (fileName.endsWith("csv"))
            return CSVLoader.loadCSV(fileName);
        else if (fileName.endsWith("xml"))
            return XMLLoader.loadXML(fileName);
        else if (fileName.endsWith("json"))
            return JSONLoader.loadJSON(fileName);
        else
            System.out.println("File type not supported.");
        return null;
    }

    public static void listUserTransactions(List<User> users, String command){
        String name = command.substring(5);
        User user = getUserByName(users, name);
        if (user == null) {
            System.out.println("User " + name + " doesn't exist.");
        }
        else {
            user.listUserTransactions();
        }
    }

    public static String getCommandFromUser(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Commads:\n1. List All\n2. List [Account]");
        System.out.print("Please enter an option: ");
        String command = sc.nextLine();
        sc.close();
        return command;
    }

    public static Logger getLogger(){
        return LOGGER;
    }

    public static double parseDoubleSafe(String amountStr){
        try{
            return Double.parseDouble(amountStr);
        } 
        catch (Exception ex){
            Main.getLogger().info("Couldn't parse " + amountStr);
            System.out.println("Couldn't parse " + amountStr + " to double.");
            return -1;
        }
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
