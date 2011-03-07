package drools.cookbook.helper;

import java.util.Random;

import drools.cookbook.model.FlightStatus;

public class FlighSimulation {

    private static final int AIRPORT_AIR_SPACE = 50;
    private Random rnd = new Random();

    private final String flight;
    private final String origin;
    private final String destination;
    private long distance;
    private boolean landed;

    public FlighSimulation(String flight, String origin, String destination, long distance) {
        this.flight = flight;
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
    }

    public FlightStatus update() {
        FlightStatus flightStatus = new FlightStatus();
        flightStatus.setFlight(flight);
        flightStatus.setDestination(destination);
        flightStatus.setOrigin(origin);
        flightStatus.setDestination(destination);
        this.distance = calculateDistance();
        flightStatus.setDistance(this.distance);
        flightStatus.setSpeed(currentSpeed());
        System.out.println("speed: " + flightStatus.getSpeed());
        return flightStatus;
    }

    private long calculateDistance() {
        if ((distance - AIRPORT_AIR_SPACE) <= AIRPORT_AIR_SPACE) {
            landed = true;
            return 0;
        }
        return distance - AIRPORT_AIR_SPACE;
    }

    private float currentSpeed() {
        return (rnd.nextFloat() * 10) + 600;
    }

    public boolean isLanded() {
        return landed;
    }

}
