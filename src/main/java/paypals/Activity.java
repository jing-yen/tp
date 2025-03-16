package paypals;

import java.util.HashMap;
import java.util.Map;

public class Activity {
    private String description;
    private String payer;
    private HashMap<String, Double> owed;
    private HashMap<String, Boolean> hasPaid;

    public Activity(String description, String payerName, HashMap<String, Double> owedMap) {
        this.description = description;
        this.payer = payerName;
        this.owed = owedMap;
        this.hasPaid = new HashMap<>();
    }

    public String getPayer() {
        return payer;
    }

    public Map<String, Double> getOwed() {
        return owed;
    }

    public boolean checkHasPaid(String name) { return hasPaid.getOrDefault(name, false); }

    public void markAsPaid(String name) { hasPaid.put(name, true); }

    public Double getAmount(String name) { return owed.get(name); }

    public String printPaidStatus(String name){
        return checkHasPaid(name) ? "[Paid]" : "[Not Paid]";
    }

    public String personToString(String name, Boolean printAmount) {
        return printAmount ? printPaidStatus(name) +  " $" + getAmount(name) + " for" :
                name + " " + printPaidStatus(name);
    }

    @Override
    public String toString() {
        String outputString = description + " paid by " + payer +
                ". Owed by: ";
        int personCount = 0;
        for (String name : owed.keySet()) {
            outputString += name;
            if (personCount++ < owed.size() - 1) {
                outputString += ", ";
            }
        }
        return outputString.toString();
    }

    public String getDescription(){
        return description;
    }

}
