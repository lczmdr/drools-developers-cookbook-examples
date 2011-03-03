package drools.cookbook.helper;

import drools.cookbook.model.FlightStatus;

public class FlighSimulation {

    private static final int DISTANCE = 50;

    private final String flight;
    private final String origin;
    private final String destination;
    private long distance;

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
        return flightStatus;
    }

    private long calculateDistance() {
        return ((distance - DISTANCE) <= 0) ? 0 : distance - DISTANCE;
    }
}
