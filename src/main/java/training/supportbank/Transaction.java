package training.supportbank;

public class Transaction {

private String date;
private String fromAccount;
private String toAccount;
private String narrative;
private double amount;

public Transaction(String date, String from, String to, String narrative, double amount){
    this.date = date;
    this.fromAccount = from;
    this.toAccount = to;
    this.narrative = narrative;
    this.amount = amount;
}

public String getDate() {
    return date;
}

public String getFrom() {
    return fromAccount;
}

public String getTo() {
    return toAccount;
}

public String getNarrative() {
    return narrative;
}

public double getAmount() {
    return amount;
}

public void setAmount(double amount) {
    this.amount = amount;
}

public void setDate(String date) {
    this.date = date;
}

public void setFrom(String from) {
    this.fromAccount = from;
}

public void setNarrative(String narrative) {
    this.narrative = narrative;
}

public void setTo(String to) {
    this.toAccount = to;
}

@Override
public String toString() {
    return "Date: " + date + 
            "\nFrom:" + fromAccount + 
            "\nTo: " + toAccount + 
            "\nNarrative: " + narrative + 
            "\nAmount: " + amount;
        }

}