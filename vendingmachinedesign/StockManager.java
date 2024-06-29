package vendingmachinedesign;

import java.util.Map;

import vendingmachinedesign.CustomException.InsufficientStockException;
import vendingmachinedesign.CustomException.ProductNotFoundException;

import java.util.List;

public class StockManager {
    private Map<Product, Integer> inventory;

    public StockManager(Map<Product, Integer> inventory) {
        this.inventory = inventory;
    }

    public boolean checkStock(List<PurchaseItem> receivedList) throws InsufficientStockException, ProductNotFoundException {
        for (PurchaseItem purchaseItem : receivedList) {
            String productName = purchaseItem.getProductName();
            int quantity = purchaseItem.getQuantity();
            Product product = findProductByName(productName);
            if (product == null) {
                throw new ProductNotFoundException("Product not found: " + productName);
            }
            if (inventory.get(product) < quantity) {
                throw new InsufficientStockException("Insufficient stock for product: " + productName);
            }
        }
        return true;
    }

    public Product findProductByName(String name) {
        for (Product product : inventory.keySet()) {
            if (product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }
        return null;
    }
}
