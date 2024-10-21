package parking;


public class ParkingSystem{
	private int totalSlots;
	private boolean[] status;
	private int currSlot = 0;


	public ParkingSystem(int totalSlots){
		this.totalSlots = totalSlots;
		this.status = new boolean[totalSlots];
	}

	public int incomingCar() {
        for (int i = currSlot; i < totalSlots; i++) {
            if (!status[i]) {
                status[i] = true;
                currSlot = i + 1;  // Update currSlot to the next possible free slot
                return i + 1;       // Return the actual slot number (1-based index)
            }
        }
        return -1;
    }

	public void exitCar(int n) {
        status[n - 1] = false;
        if (n - 1 < currSlot) {
            currSlot = n - 1;  
        }
    }

	
}