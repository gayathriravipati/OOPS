package vendingmachinedesign;

import java.util.List;

public class AmountCalculator {
    private List<Product> productDetails;

    public AmountCalculator(List<Product> productDetails) {
        this.productDetails = productDetails;
    }

    public double calculateTotal(List<PurchaseItem> receivedList) {
        double totalAmount = 0.0;

        for (PurchaseItem purchaseItem : receivedList) {
            String productName = purchaseItem.getProductName();
            int quantity = purchaseItem.getQuantity();
            Product product = getProductByName(productName);

            if (product != null) {
                double productPrice = product.getPrice();
                totalAmount += productPrice * quantity;
            } else {
                System.out.println("Product not found: " + productName);
            }
        }

        return totalAmount;
    }

    private Product getProductByName(String name) {
        for (Product product : productDetails) {
            if (product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }
        return null; 
    }
}
