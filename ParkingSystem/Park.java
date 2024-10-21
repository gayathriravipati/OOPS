package parking;

public class Park{
	public static void main(String[] args){
		ParkingSystem ps = new ParkingSystem(10);
		int slot = ps.incomingCar();
		if(slot == -1){
			System.out.println("Slots are full");
		}
		else{
			System.out.println("Allocated slot is " + slot);
		}

		ps.exitCar(1);


		 slot = ps.incomingCar();

		if(slot == -1){
			System.out.println("Slots are full");
		}
		else{
			System.out.println("Allocated slot is " + slot);
		}

	}
}

