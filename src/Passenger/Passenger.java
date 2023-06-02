package Passenger;

import java.util.Random;
import java.util.UUID;

public class Passenger {
    private static int uniqueId;
    private int passengerID;
    private String passengerDocument;
  private boolean hasLuggage;

    public boolean getHasLuggage() {
        return hasLuggage;
    }

    public void setHasLuggage(boolean hasLuggage) {
        this.hasLuggage = hasLuggage;
    }

    public Passenger()
    {
        this.passengerID=uniqueId++;
        UUID uuid = UUID.randomUUID();
        passengerDocument=uuid.toString();
        Random random = new Random();
        this.hasLuggage= random.nextDouble()<=0.7;
    }

    public Passenger(int passengerID, String passengerDocument) {
        this.passengerID = passengerID;
        this.passengerDocument = passengerDocument;
        Random random = new Random();
        this.hasLuggage= random.nextDouble()<=0.7;
    }

    public int getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(int passengerID) {
        this.passengerID = passengerID;
    }

    public String getPassengerDocument() {
        return passengerDocument;
    }

    public void setPassengerDocument(String passengerDocument) {
        this.passengerDocument = passengerDocument;
    }

    public boolean hasUnallowedItemsInLuggage() {
        Random random = new Random();
        return random.nextDouble()<=0.1;
    }
}
