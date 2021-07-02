package training.supportbank;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransactionUtil {

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

    public static List<Transaction> extractTransactionsFromUsers(List<User> users){
        Set<Transaction> tr = new HashSet<>();
        for(User user : users){
            for (Transaction trans : user.getTransactions()){
                tr.add(trans);
            }
        }
        return new ArrayList<>(tr);
    }

}
