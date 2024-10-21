import java.util.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class ParkingLotDemo {
    public static void main(String[] args) {
        try {
            ParkingLot parkingLot = ParkingLot.getInstance(); 
            parkingLot.addLevel(new Level(1, 6)); // Level 1
            parkingLot.addLevel(new Level(2, 8)); // Level 2

            Vehicle car = new Car("ABC123");
            Vehicle truck = new Truck("XYZ789");
            Vehicle motorcycle = new Motorcycle("M1234");

            parkingLot.displayAvailability();

            // Park vehicles
            parkingLot.parkVehicle(car, 1);        
            parkingLot.parkVehicle(truck, 2);      
            parkingLot.parkVehicle(motorcycle, 0); 

            parkingLot.displayAvailability();

            // Unpark vehicle
            parkingLot.unparkVehicle(motorcycle);

            // Display updated availability
            parkingLot.displayAvailability();

            // Calculate charge for car exit
            parkingLot.unparkVehicle(car);
        } catch (ParkingLotException e) {
            System.err.println(e.getMessage());
        }
    }
}

class ParkingLot {
    private static ParkingLot instance;
    private final List<Level> levels;
    private static int totalParkedVehicles = 0; // Static variable to keep track of total parked vehicles
    public static final List<Integer> BLOCKED_ENTRY_POINTS = Arrays.asList(0, 1, 2); 
    
    private ParkingLot() {
        levels = new ArrayList<>();
    }

    public static synchronized ParkingLot getInstance() {
        if (instance == null) {
            instance = new ParkingLot();
        }
        return instance;
    }

    public void addLevel(Level level) {
        levels.add(level);
    }

    public boolean parkVehicle(Vehicle vehicle, int entryPoint) throws ParkingLotException {
        if (!isValidEntryPoint(entryPoint)) {
            throw new ParkingLotException("Error: Entry point " + entryPoint + " is invalid.");
        }

        for (Level level : levels) {
            if (level.parkVehicle(vehicle, entryPoint)) {
                totalParkedVehicles++; 
                System.out.println("Vehicle parked at Level " + level.getFloor() + ". Total parked vehicles at this lot: " + totalParkedVehicles);
                return true;
            }
        }
        throw new ParkingLotException("Error: No available spots for vehicle " + vehicle.getLicensePlate() + ".");
    }

    public boolean unparkVehicle(Vehicle vehicle) throws ParkingLotException {
        for (Level level : levels) {
            if (level.unparkVehicle(vehicle)) {
                totalParkedVehicles--; 
                System.out.println("Vehicle removed from Level " + level.getFloor() + ". Total parked vehicles at this lot: " + totalParkedVehicles);
                return true;
            }
        }
        throw new ParkingLotException("Error: Vehicle " + vehicle.getLicensePlate() + " not found in the parking lot.");
    }

    public void displayAvailability() {
        for (Level level : levels) {
            level.displayAvailability();
        }
        System.out.println("Total parked vehicles in the parking lot: " + totalParkedVehicles);
    }

    private boolean isValidEntryPoint(int entryPoint) {
        return BLOCKED_ENTRY_POINTS.contains(entryPoint); 
    }
}


// Level Class
class Level {
    private final int floor;
    private final List<ParkingSpot> parkingSpots;
    private int parkedVehicles = 0;
    private final PriorityQueue<Integer> availableSlots; 

    public Level(int floor, int numSpots) {
        this.floor = floor;
        parkingSpots = new ArrayList<>(numSpots);
        availableSlots = new PriorityQueue<>(); 

        for (int i = 0; i < numSpots; i++) {
            if (!ParkingLot.BLOCKED_ENTRY_POINTS.contains(i)) {
                VehicleType type = i % 3 == 0 ? VehicleType.TRUCK : (i % 2 == 0 ? VehicleType.MOTORCYCLE : VehicleType.CAR);
                ParkingSpot spot = new ParkingSpot(i, type);
                parkingSpots.add(spot);
                availableSlots.offer(spot.getSpotNumber()); 
            }
        }
    }

    private Set<Integer> recentlyTriedSpots = new HashSet<>();  // Tracks recently tried spots

public synchronized boolean parkVehicle(Vehicle vehicle, int entryPoint) {
    while (!availableSlots.isEmpty()) {
        Integer spotNumber = availableSlots.poll(); 
        if (spotNumber == null) {
            break;
        }

        if (recentlyTriedSpots.contains(spotNumber)) {
            availableSlots.offer(spotNumber);  
            continue;  
        }

        if (spotNumber < parkingSpots.size()) {
            ParkingSpot spot = parkingSpots.get(spotNumber);

            if (spot.isAvailable() && spot.getVehicleType() == vehicle.getType()) {
                spot.parkVehicle(vehicle);  
                spot.setEntryTime(LocalDateTime.now());  
                parkedVehicles++;  

=                recentlyTriedSpots.remove(spotNumber);
                System.out.println("Vehicle " + vehicle.getLicensePlate() +
                                   " parked at spot " + spot.getSpotNumber() +
                                   " on Level " + floor);
                return true;  
            } else {
                availableSlots.offer(spotNumber);
                recentlyTriedSpots.add(spotNumber);  
                System.out.println("Spot " + spotNumber +
                                   " is either occupied or type mismatch for vehicle " +
                                   vehicle.getLicensePlate());
            }
        }
    }
    recentlyTriedSpots.clear();
    return false;  // Return false if no suitable spot was found
}

    public synchronized boolean unparkVehicle(Vehicle vehicle) {
        for (ParkingSpot spot : parkingSpots) {
            if (!spot.isAvailable() && spot.getParkedVehicle().equals(vehicle)) {
                spot.unparkVehicle();
                parkedVehicles--;
                availableSlots.offer(spot.getSpotNumber()); // Add freed spot back to min-heap
                Duration parkedDuration = Duration.between(spot.getEntryTime(), LocalDateTime.now());
                long hoursParked = Math.max(1, parkedDuration.toHours());
                double charge = calculateCharge(hoursParked, vehicle.getType());

                System.out.println("Vehicle " + vehicle.getLicensePlate() + " removed from spot " + spot.getSpotNumber() 
                                    + " on Level " + floor + ". Charge: $" + charge);
                return true;
            }
        }
        return false;
    }

    public void displayAvailability() {
        System.out.println("Level " + floor + " Availability:");
        for (ParkingSpot spot : parkingSpots) {
            System.out.println("Spot " + spot.getSpotNumber() + " (" + spot.getVehicleType() + "): " + (spot.isAvailable() ? "Available" : "Occupied"));
        }
        System.out.println("Total parked vehicles at Level " + floor + ": " + parkedVehicles);
    }

    public int getFloor() {
        return floor;
    }

    private double calculateCharge(long hours, VehicleType type) {
        switch (type) {
            case CAR:
                return hours * 2.0;
            case TRUCK:
                return hours * 3.5;
            case MOTORCYCLE:
                return hours * 1.0;
            default:
                return hours * 2.0;
        }
    }
}

// Parking Spot Class
class ParkingSpot {
    private final int spotNumber;
    private final VehicleType vehicleType;
    private Vehicle parkedVehicle;
    private LocalDateTime entryTime;
    

    public ParkingSpot(int spotNumber, VehicleType vehicleType) {
        this.spotNumber = spotNumber;
        this.vehicleType = vehicleType;
    }

    public synchronized boolean isAvailable() {
        return parkedVehicle == null;
    }

    public synchronized void parkVehicle(Vehicle vehicle) {
        if (isAvailable() && vehicle.getType() == vehicleType) {
            parkedVehicle = vehicle;
        } else {
            throw new IllegalArgumentException("Invalid vehicle type or spot already occupied.");
        }
    }

    public synchronized void unparkVehicle() {
        parkedVehicle = null;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    public int getSpotNumber() {
        return spotNumber;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }
}

// Vehicle, Car, Motorcycle, Truck classes
enum VehicleType {
    CAR,
    MOTORCYCLE,
    TRUCK
}

abstract class Vehicle {
    private final String licensePlate;
    private final VehicleType type;

    public Vehicle(String licensePlate, VehicleType type) {
        this.licensePlate = licensePlate;
        this.type = type;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getType() {
        return type;
    }
}

class Car extends Vehicle {
    public Car(String licensePlate) {
        super(licensePlate, VehicleType.CAR);
    }
}

class Motorcycle extends Vehicle {
    public Motorcycle(String licensePlate) {
        super(licensePlate, VehicleType.MOTORCYCLE);
    }
}

class Truck extends Vehicle {
    public Truck(String licensePlate) {
        super(licensePlate, VehicleType.TRUCK);
    }
}


// Exception Class
class ParkingLotException extends Exception {
    public ParkingLotException(String message) {
        super(message);
    }
}
