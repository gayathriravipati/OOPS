import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Orders pizza and return the cost of the order based on base and toppingds selected

class Base {
    private String baseName;
    private Double cost;

    public Base(String baseName, Double cost) {
        this.baseName = baseName;
        this.cost = cost;
    }

    public String getBaseName() {
        return baseName;
    }

    public Double getCost() {
        return cost;
    }
}

class Toppings {
    private String toppingName;
    private Double cost;

    public Toppings(String toppingName, Double cost) {
        this.toppingName = toppingName;
        this.cost = cost;
    }

    public String getToppingName() {
        return toppingName;
    }

    public Double getCost() {
        return cost;
    }
}


class PizzaSystem {
    private List<Base> baseDetails;
    private List<Toppings> toppingDetails;
    public PizzaSystem() {
        baseDetails = new ArrayList<>();
        toppingDetails = new ArrayList<>();
        initialiseBaseDetails();
        initialiseToppingDetails();
        displayMenu();
    }

    void initialiseBaseDetails() {
        baseDetails.add(new Base("Thin Crust", 2.5));
        baseDetails.add(new Base("Normal Crust", 3.5));
        baseDetails.add(new Base("Italian Crust", 4.5));
    }

    void initialiseToppingDetails() {
        toppingDetails.add(new Toppings("Onion", 1.0));
        toppingDetails.add(new Toppings("Mushrooms", 2.0));
        toppingDetails.add(new Toppings("Olives", 3.0));
        toppingDetails.add(new Toppings("Chicken", 4.0));
    }

    private void displayMenu() {
        System.out.println("Available bases:");
        for (Base base : baseDetails) {
            System.out.println(base.getBaseName() + " ($" + base.getCost() + ")");
        }

        System.out.println("Available toppings:");
        for (Toppings topping : toppingDetails) {
            System.out.println(topping.getToppingName() + " ($" + topping.getCost() + ")");
        }
    }

    public Base getBase(String baseName) {
        for (Base base : baseDetails) {
            if (base.getBaseName().equals(baseName)) {
                return base;
            }
        }
        return null;
    }

    public Toppings getTopping(String toppingName) {
        for (Toppings topping : toppingDetails) {
            if (topping.getToppingName().equals(toppingName)) {
                return topping;
            }
        }
        return null;
    }

    public void createOrder(String baseName, List<String> toppingNames) {
        Base base = getBase(baseName);
        List<Toppings> toppings = new ArrayList<>();
        for (String toppingName : toppingNames) {
            Toppings topping = getTopping(toppingName);
            if (topping != null) {
                toppings.add(topping);
            }
        }

        if (base != null && !toppings.isEmpty()) {
            Order order = new Order(base, toppings);
        } else {
            System.out.println("Error in order. Base or toppings not found.");
        }
    }

}

class Order{
    private Base baseName;
    private List<Toppings> toppingNames;

    public Order(Base baseName, List<Toppings> toppingNames){
        this.baseName = baseName;
        this.toppingNames = toppingNames;
        displayOrderDetails();
        calculateCost();
    }

    public void displayOrderDetails(){
        System.out.println("Here are your order details");
        System.out.println("Base is" + " " + baseName.getBaseName());
        for(Toppings topping: toppingNames){
            System.out.println(topping.getToppingName());
        }
    }

    public double calculateCost(){
        double totalCost = baseName.getCost();
        for(Toppings topping: toppingNames){
            totalCost += topping.getCost();
        }
        System.out.println("Total Cost: $" + totalCost);
        return totalCost;
    }

}

public class PizzaOrder {
    public static void main(String[] args) {
        PizzaSystem ps = new PizzaSystem();
        ps.createOrder("Thin Crust", Arrays.asList("Onion", "Mushrooms"));
}
}