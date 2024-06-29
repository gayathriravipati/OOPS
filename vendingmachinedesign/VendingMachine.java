package vendingmachinedesign;

import java.util.*;

public class VendingMachine {
    public static void main(String[] args) {
        // Get the singleton instance of InitialiseVendingMachine
        InitialiseVendingMachine vendingMachine = InitialiseVendingMachine.getInstance();
        System.out.println("Initialization is successful");

        List<PurchaseItem> receivedList = new ArrayList<>();
        receivedList.add(new PurchaseItem("Coke", 2));
        receivedList.add(new PurchaseItem("Chips", 1));
//        receivedList.add(new PurchaseItem("Monster", 2));

        //Utilising enums as there can be different types of currencies
        List<Amount> payment = new ArrayList<>();
        payment.add(Amount.FIVE);
        payment.add(Amount.FIVE); 

        double receivedAmount = calculateTotalAmount(payment);

        System.out.println("Total received amount: " + receivedAmount);

        boolean success = vendingMachine.purchaseProducts(receivedList, receivedAmount);
        if (success) {
            System.out.println("Purchase completed successfully.");
            vendingMachine.displayInventory();
        } else {
            System.out.println("Purchase failed.");
        }
    }

    public static double calculateTotalAmount(List<Amount> amounts) {
        double total = 0;
        for (Amount amount : amounts) {
            total += amount.getValue();
        }
        return total;
    }
}
