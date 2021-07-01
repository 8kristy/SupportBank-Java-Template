package training.supportbank;

import java.util.List;
import java.util.ArrayList;

public class User{
    String name;
    List<Transaction> transactions;
    double netMoney;

    public String getName() {
        return name;
    }

    public double getNetMoney() {
        return netMoney;
    }

    public User(String name){
        this.name = name;
        transactions = new ArrayList<>();
    }

    // Transaction where user is receiving money
    public void addTransactionTo(Transaction transaction){
        transactions.add(transaction);
        netMoney += transaction.getAmount();
    }

    // Transaction where user is sending money
    public void addTransactionFrom(Transaction transaction){
        transactions.add(transaction);
        netMoney -= transaction.getAmount();
    }

    public void listUserTransactions(){
        transactions.stream().forEach((Transaction tr) -> printTransaction(tr));
    }

    private void printTransaction(Transaction tr){
        double amount = tr.getAmount();
        if (tr.getFrom().equals(this.name))
            amount = -1 * amount;
        System.out.println(tr.getDate() + " " + tr.getNarrative() + " " + amount);
    }


}