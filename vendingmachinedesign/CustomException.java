package vendingmachinedesign;

public class CustomException{

    public static class InsufficientStockException extends Exception {
        public InsufficientStockException(String message) {
            super(message);
        }
    }

    public static class ProductNotFoundException extends Exception {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }
    
    public static class AmountNotSufficientException extends Exception {
        public AmountNotSufficientException(String message) {
            super(message);
        }
    }
    
    
    
    
}
