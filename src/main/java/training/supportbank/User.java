import java.util.List;

public class User{
    String name;
    List<Transaction> transactions;
    Double netMoney;

    public User(String name){
        this.name = name;
        transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
        netMoney += transaction.getAmount();
    }

}