package vendingmachinedesign;

public class PurchaseItem {
    private String productName;
    private int quantity;

    public PurchaseItem(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}
}

