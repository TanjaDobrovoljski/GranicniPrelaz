package Passenger;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

public class Passenger implements Serializable {
    private static int uniqueId;
    private int passengerID;
    private String passengerDocument;
  private boolean hasLuggage,hasUnallowedItems,isDriver,hasValidDocuments;

    public boolean isHasValidDocuments() {
        return hasValidDocuments;
    }

    public void setHasValidDocuments(boolean hasValidDocuments) {
        this.hasValidDocuments = hasValidDocuments;
    }

    public boolean isHasLuggage() {
        return hasLuggage;
    }

    public boolean isDriver() {
        return isDriver;
    }

    public void setDriver(boolean driver) {
        isDriver = driver;
    }

    public boolean getHasLuggage() {
        return  hasLuggage;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "passengerID=" + passengerID +
                ", passengerDocument='" + passengerDocument + '\'' +
                ", hasLuggage=" + hasLuggage +
                ", hasUnallowedItems=" + hasUnallowedItems +
                ", isDriver=" + isDriver +
                ", hasValidDocuments=" + hasValidDocuments +
                '}';
    }

    public Passenger(boolean isDriver) {
        this.passengerID = uniqueId++;
        UUID uuid = UUID.randomUUID();
        this.passengerDocument=uuid.toString();
        Random random = new Random();
        this.hasLuggage= random.nextDouble()<=0.7;
        this.isDriver=isDriver;
        this.hasValidDocuments=true;
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

    public boolean gethasUnallowedItemsInLuggage() {
       return hasUnallowedItems;
    }
    public void setHasUnallowedItems(boolean has)
    {
        this.hasUnallowedItems=has;
    }
}
