package training.supportbank;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String args[]) throws Exception {
        if (args.length > 0){
            List<User> users = UserService.getFileNameFromUser(args[0]);
            if (users != null){
                String command = UserService.getCommandFromUser();
                if (command.equals("List All"))
                    UserService.ListAll(users);
                else if (command.contains("List"))
                    UserService.listUserTransactions(users, command);
                else if (command.contains("Export")){
                    List<Transaction> tr = TransactionUtil.extractTransactionsFromUsers(users);
                    String filename = command.substring(command.indexOf(" ") + 1);
                    UserService.exportTransactions(filename, tr);
                }
                else
                    System.out.println("Invalid command.");
            }
        }
        else
            System.out.println("Filename not given in arguments.");
    } 


    public static Logger getLogger(){
        return LOGGER;
    }
}
