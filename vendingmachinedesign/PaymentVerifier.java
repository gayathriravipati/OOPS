package vendingmachinedesign;

import vendingmachinedesign.CustomException.AmountNotSufficientException;

public class PaymentVerifier {

    public boolean verifyPayment(double receivedAmount, double totalAmount) throws AmountNotSufficientException {
        if (receivedAmount < totalAmount) {
            throw new AmountNotSufficientException("Error: Insufficient payment. Received: " + receivedAmount + ", Required: " + totalAmount);
        } else if (receivedAmount > totalAmount) {
            double change = receivedAmount - totalAmount;
            System.out.println("Payment verified. Change to be given: " + change);
            return true;
        } else {
            System.out.println("Payment verified. Exact payment received.");
            return true;
        }
    }
}
