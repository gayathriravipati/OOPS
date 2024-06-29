package vendingmachinedesign;

public enum Amount {
	PENNY(0.01),
    NICKEL(0.05),
    DIME(0.1),
    QUARTER(0.25),
	ONE(1),
    FIVE(5),
    TEN(10),
    TWENTY(20);
	
	private final double value;

	Amount(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

}
