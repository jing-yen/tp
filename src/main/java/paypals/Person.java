package paypals;

public class Person {
    private String name;
    private double amount;
    private boolean hasPaid;

    public Person(String name, double amount, boolean hasPaid) {
        this.name = name;
        this.amount = amount;
        this.hasPaid = hasPaid;
    }

    public String getName() {
        return name;
    }

    public void editName(String name) {
        this.name = name;
    }

    public void editAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void addAmount(double amount) {
        this.amount += amount;
    }

    public void markAsPaid() {
        this.hasPaid = true;
    }

    public boolean hasPaid() {
        return hasPaid;
    }

    public String toString(boolean printAmount) {
        return printAmount ? "$" + String.format("%.2f", amount) + " " + printPaidStatus() :
                name + " " + printPaidStatus();
    }

    public String printPaidStatus(){
        return hasPaid ? "[Paid]" : "[Unpaid]";
    }
}
