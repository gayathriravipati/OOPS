package vendingmachinedesign;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import vendingmachinedesign.CustomException.InsufficientStockException;
import vendingmachinedesign.CustomException.ProductNotFoundException;
import vendingmachinedesign.CustomException.AmountNotSufficientException;

import java.util.Map;

public class InitialiseVendingMachine {
    private List<Product> productDetails;
    private Map<Product, Integer> inventoryCount; 
    
    //Following Singleton design pattern
    private static InitialiseVendingMachine instance;
    private InitialiseVendingMachine() {
    	productDetails = new ArrayList<>();
    	inventoryCount = new ConcurrentHashMap<>();
    	initialiseProducts();
    	displayInventory();
    }
    
    public static synchronized InitialiseVendingMachine getInstance() {
        if (instance == null) {
            instance = new InitialiseVendingMachine();
        }
        return instance;
    }
    
    //To get the system initialized with the basic products required
    void initialiseProducts() {
    	Product coke = new Product("Coke",2.0);
    	Product monster = new Product("Monster",4.5);
    	Product chips = new Product("Chips",1.5);
    	
    	productDetails.add(coke);
    	productDetails.add(monster);
    	productDetails.add(chips);
    	
    	addProductToInventory(coke,5);
    	addProductToInventory(monster,1);
    	addProductToInventory(chips,5);
    	addProductToInventory(coke,1);
    }
    
    //Adding products based on the given count
    void addProductToInventory(Product product, int count) {
    	inventoryCount.put(product, inventoryCount.getOrDefault(product, 0) + count);
    }
    
    void displayInventory() {
    	for(Product product : inventoryCount.keySet()) {
    		System.out.println("Product: " + product.getName() + ", Price: " + product.getPrice() + ", Count: " + inventoryCount.get(product));
    	}
    }
    
    
    public boolean purchaseProducts(List<PurchaseItem> receivedList, double receivedAmount) {
        StockManager stockManager = new StockManager(inventoryCount);
        AmountCalculator amountCalculator = new AmountCalculator(productDetails);
        PaymentVerifier paymentVerifier = new PaymentVerifier();

        try {
            // Step 1: Check if stock is available
            stockManager.checkStock(receivedList);

            double totalAmount = amountCalculator.calculateTotal(receivedList);

            paymentVerifier.verifyPayment(receivedAmount, totalAmount);

            
            for (PurchaseItem item : receivedList) {
                String productName = item.getProductName();
                int quantity = item.getQuantity();
                Product product = stockManager.findProductByName(productName);

                if (product != null) {
                    int currentStock = inventoryCount.get(product);
                    inventoryCount.put(product, currentStock - quantity);
                }
            }
            System.out.println("Purchase processed successfully.");
            return true;
        }  catch (InsufficientStockException | ProductNotFoundException | AmountNotSufficientException e) {
            System.out.println(e.getMessage());
            return false;        }
    }
}

