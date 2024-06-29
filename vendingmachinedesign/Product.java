package vendingmachinedesign;

public class Product {
	private final String name;
    private final double price;
    
    public Product(String name, double price) {
    	this.name = name;
        this.price = price;
    }
    
    public String getName(){
    	return name;
    }
    
    public Double getPrice() {
    	return price;
    }
}
