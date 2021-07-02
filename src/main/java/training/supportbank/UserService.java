package training.supportbank;

import java.util.List;
import java.util.Scanner;

public class UserService {
    public static String getCommandFromUser(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Commads:\n1. List All\n2. List [Account]\n3. Export [Filename]");
        System.out.print("Please enter an option: ");
        String command = sc.nextLine();
        sc.close();
        return command;
    }

    public static void ListAll(List<User> users){
        users.stream().forEach((user) -> System.out.println(user.getName() + ": "
                                             + Math.round(user.getNetMoney()*100.0)/100.0));
    }

    public static void listUserTransactions(List<User> users, String command){
        String name = command.substring(5);
        User user = TransactionUtil.getUserByName(users, name);
        if (user == null) {
            System.out.println("User " + name + " doesn't exist.");
        }
        else {
            user.listUserTransactions();
        }
    }
  
    public static void exportTransactions(String filename, List<Transaction> tr){
        if (filename.endsWith("csv"))
            ExportService.writeCSV(filename, tr);
        else if (filename.endsWith("xml"))
            ExportService.writeXML(filename, tr);
        else if (filename.endsWith("json"))
            ExportService.writeJSON(filename, tr);
        else
            System.out.println("File type not supported.");
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
}
